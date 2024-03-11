package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.ChannelSaveDto;
import com.heima.model.wemedia.dtos.WmChannelPageReqDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {


    @Autowired
    WmChannelMapper wmChannelMapper;
    /**
     * 查询所有频道
     * @return
     */
    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }

    @Override
    public ResponseResult deleteById(Integer id) {
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        wmChannelMapper.deleteById(id);

        return ResponseResult.okResult(id);
    }

    @Override
    public ResponseResult save(ChannelSaveDto dto) {
        if(dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmChannel wmChannel = new WmChannel();
        BeanUtils.copyProperties(dto,wmChannel);
        wmChannel.setCreatedTime(new Date());

        wmChannelMapper.insert(wmChannel);

        return ResponseResult.okResult(wmChannel);
    }

    @Override
    public ResponseResult pageList(WmChannelPageReqDto dto){
        dto.checkParam();

        IPage page = new Page(dto.getPage(),dto.getSize());

        LambdaQueryWrapper<WmChannel> lambdaQueryWrapper =  new LambdaQueryWrapper<>();

        if(dto.getName() != null && StringUtils.isNotBlank(dto.getName())){
            lambdaQueryWrapper.eq(WmChannel::getName,dto.getName());
        }

        page = page(page,lambdaQueryWrapper);

        //结果fan'hui
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(),(int)page.getTotal());
        responseResult.setData(page.getRecords());

        return responseResult;

    }

    @Override
    public ResponseResult updateChannel(ChannelSaveDto dto) {

        if(dto == null || dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmChannel wmChannel = new WmChannel();

        BeanUtils.copyProperties(dto,wmChannel);

        wmChannelMapper.updateById(wmChannel);

        return ResponseResult.okResult(wmChannel);

    }

}