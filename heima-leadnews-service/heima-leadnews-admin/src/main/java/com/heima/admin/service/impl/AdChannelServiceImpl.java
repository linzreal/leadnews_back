package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.wmMapper.ChannelMapper;
import com.heima.admin.service.AdChannelService;
import com.heima.model.admin.dtos.ChannelSaveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdChannelServiceImpl extends ServiceImpl<ChannelMapper, WmChannel> implements AdChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public ResponseResult save(ChannelSaveDto dto) {

        if(!checkParams(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmChannel wmChannel = new WmChannel();
        BeanUtils.copyProperties(dto,wmChannel);

        channelMapper.insert(wmChannel);

        return ResponseResult.okResult(wmChannel);
    }

    @Override
    public ResponseResult delById(Integer id) {
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        channelMapper.deleteById(id);

        return ResponseResult.okResult(id);


    }


    private boolean checkParams(ChannelSaveDto dto){
        if(dto == null){
            return false;
        } else {
            return true;
        }
    }

}
