package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.ChannelSaveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;

public interface AdChannelService extends IService<WmChannel> {
    public ResponseResult save(ChannelSaveDto dto);

    ResponseResult delById(Integer id);
}
