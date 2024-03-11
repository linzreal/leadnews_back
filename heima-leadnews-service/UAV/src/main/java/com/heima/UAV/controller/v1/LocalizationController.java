package com.heima.UAV.controller.v1;

import com.heima.UAV.service.LocalizationService;
import com.heima.model.UAV.dtos.LocalizationDto;
import com.heima.model.UAV.dtos.LocalizationPageReqDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Localization")
public class LocalizationController {

    @Autowired
    private LocalizationService localizationService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody LocalizationDto dto){
        return localizationService.add(dto);
    }

    @PostMapping("/query")
    public ResponseResult query(@RequestBody LocalizationPageReqDto dto){
        return null;
    }


}
