package com.heima.UAV.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.UAV.dtos.LocalizationDto;
import com.heima.model.UAV.dtos.LocalizationPageReqDto;
import com.heima.model.UAV.pojos.Localization;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface LocalizationService extends IService<Localization> {

    public ResponseResult add(LocalizationDto dto);

    public ResponseResult query(LocalizationPageReqDto dto);

}
