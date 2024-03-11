package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heima.apis.schedule.IScheduleClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.TaskTypeEnum;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.common.ProtostuffUtil;
import com.heima.wemedia.service.WmNewsAutoScanService;
import com.heima.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {

    @Autowired
    private IScheduleClient scheduleClient;

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Override
    @Async("myExecutor")
    public void addNewsToTask(Integer id, Date publishTime) {

        log.info("添加任务到延时发布中——begin");

        Task task = new Task();
        task.setExecuteTime(publishTime.getTime());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());

        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        task.setParameters(ProtostuffUtil.serialize(wmNews));

        scheduleClient.addTask(task);

        log.info("添加任务到延时发布中——end");
    }

    @Scheduled(fixedRate = 1000)
    @Override
    public void scanNewsByTime(){

//        log.info("消费任务，审核文章");
        ResponseResult responseResult = scheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());

        if(responseResult.getCode().equals(200) && responseResult.getData() != null){
            Task task = JSONObject.parseObject(JSONObject.toJSONString(responseResult.getData()),Task.class);

            WmNews wmNews = ProtostuffUtil.deserialize(task.getParameters(),WmNews.class);

            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        }

    }
}
