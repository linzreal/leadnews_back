package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.dtos.WmSensitivePageReqDto;
import com.heima.model.wemedia.pojos.WmSensitive;


public interface WmSensitiveService extends IService<WmSensitive> {

    public ResponseResult save(WmSensitiveDto dto);

    public ResponseResult list(WmSensitivePageReqDto dto);

    public ResponseResult del(Integer id);

    public ResponseResult updateSensitive(WmSensitiveDto dto);


}
