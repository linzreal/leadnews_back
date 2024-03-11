package com.heima.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.ScheduleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.schedule.pojos.Taskinfo;
import com.heima.model.schedule.pojos.TaskinfoLogs;
import com.heima.schedule.mapper.TaskinfoLogsMapper;
import com.heima.schedule.mapper.TaskinfoMapper;
import com.heima.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService  {


    @Autowired
    private CacheService cacheService;

    @Autowired
    private TaskinfoMapper taskinfoMapper;

    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;

    /**
     * 添加任务
     * @param task
     * @return
     */
    @Override
    public long addTask(Task task) {

        boolean success  = addTaskToDb(task);
        if(success){
            addTaskToCache(task);
        }

        return task.getTaskId();
    }

    private void addTaskToCache(Task task) {
        String key = task.getTaskType()+"_"+task.getPriority();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);
        long nextScheduleTime = calendar.getTimeInMillis();

        long thisTime = System.currentTimeMillis();

        if(task.getExecuteTime() <= thisTime){
            cacheService.lLeftPush(ScheduleConstants.TOPIC+key, JSON.toJSONString(task));
        }else if(task.getExecuteTime() <= nextScheduleTime){
            cacheService.zAdd(ScheduleConstants.FUTURE+key,JSON.toJSONString(task),task.getExecuteTime());
        }
    }

    private boolean addTaskToDb(Task task) {
        boolean flag = false;

        try{
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task,taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);
            task.setTaskId(taskinfo.getTaskId());


            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(task,taskinfoLogs);
            taskinfoLogs.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoLogs.setVersion(1);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);
            flag = true;

        } catch (Exception e){
            e.printStackTrace();
        }

        return flag;
    }


    /**
     * 取消任务
     */
    @Override
    public boolean cancelTask(long taskId){
        boolean flag = false;

        //删除任务，更新日志
        Task task = updateDb(taskId,ScheduleConstants.CANCELLED);

        if(task!=null){
            removeTaskFromRedis(task);
            flag =true;
        }

        return flag;

    }

    private void removeTaskFromRedis(Task task) {
        String key = task.getTaskType()+"_"+task.getPriority();

        if(task.getExecuteTime()<=System.currentTimeMillis()){
            cacheService.lRemove(ScheduleConstants.TOPIC+key,0,JSON.toJSONString(task));
        }else{
            cacheService.zRemove(ScheduleConstants.FUTURE+key,JSON.toJSONString(task));
        }
    }



    private Task updateDb(long taskId, int status) {

        Task task = null;

        try{
            taskinfoMapper.deleteById(taskId);

            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);

            taskinfoLogs.setStatus(status);
            taskinfoLogsMapper.updateById(taskinfoLogs);

            task = new Task();
            BeanUtils.copyProperties(taskinfoLogs,task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());


        }catch (Exception e){
            log.error("task canel error , taskid:{}",taskId);
        }

        return task;
    }


    /**
     * 根据类型喝优先级拉去任务
     * @param type
     * @param priority
     * @return
     */
    @Override
    public Task poll(int type, int priority){
        Task task = null;

        try{
            String key = type+"_"+priority;
            String taskJson = cacheService.lRightPop(ScheduleConstants.TOPIC+key);
            if(StringUtils.isNotBlank(taskJson)){
                task = JSON.parseObject(taskJson,Task.class);

                updateDb(task.getTaskId(),ScheduleConstants.EXECUTED);
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error("poll task error");
        }

        return task;
    }


    /**
     * 未来数据定时刷新，每分钟刷新一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh(){


        String token = cacheService.tryLock("FUTURE_TASK_SYNC", 1000 * 30);
        if(StringUtils.isNotBlank(token)){
            log.info("未来数据定时刷新----定时任务");
            //获取所有未来数据的集合
            Set<String> keys = cacheService.scan(ScheduleConstants.FUTURE + "*");

            for(String key : keys){

                String topicKey = ScheduleConstants.TOPIC+key.split(ScheduleConstants.FUTURE)[1];

                //按照key和分值查看符合条件的数据
                Set<String> tasks = cacheService.zRangeByScore(key, 0, System.currentTimeMillis());

                //同步数据
                if(!tasks.isEmpty()){
                    cacheService.refreshWithPipeline(key,topicKey,tasks);
                    log.info("成功将"+key+"刷新到了"+topicKey);
                }

            }
        }

    }


    /**
     * 数据库任务定时同步到redis
     */
    @PostConstruct
    @Scheduled(cron = "0 */5 * * * ?")
    public void reloadDate(){

        //删除缓存
        Set<String> topicKeys = cacheService.scan(ScheduleConstants.TOPIC + "*");
        Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");

        cacheService.delete(topicKeys);
        cacheService.delete(futureKeys);


        //查询符合条件的任务
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);
        List<Taskinfo> taskinfoList = taskinfoMapper.selectList(Wrappers.<Taskinfo>lambdaQuery().lt(Taskinfo::getExecuteTime, calendar.getTime()));

        //任务添加到redis
        if(taskinfoList != null && taskinfoList.size() > 0){
            for(Taskinfo taskinfo : taskinfoList){
                Task task = new Task();
                BeanUtils.copyProperties(taskinfo,task);
                task.setExecuteTime(taskinfo.getExecuteTime().getTime());
                addTaskToCache(task);
            }
        }

        log.info("数据库的任务同步到了redis");
    }


}
