package com.heima.UAV.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.UAV.mapper.LocalizationMapper;
import com.heima.UAV.service.LocalizationService;
import com.heima.model.UAV.dtos.LocalizationDto;
import com.heima.model.UAV.dtos.LocalizationPageReqDto;
import com.heima.model.UAV.pojos.Localization;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class LocalizationServiceImpl extends ServiceImpl<LocalizationMapper, Localization> implements LocalizationService {
    @Autowired
    private LocalizationMapper localizationMapper;

    @Override
    public ResponseResult add(LocalizationDto dto) {

        Localization localization = new Localization();

        BeanUtils.copyProperties(dto,localization);

        localizationMapper.insert(localization);

        return ResponseResult.okResult(localization);
    }

    @Override
    public ResponseResult query(LocalizationPageReqDto dto) {
        dto.checkParam();

        IPage page = new Page(dto.getPage(), dto.getSize());

        LambdaQueryWrapper<Localization> lambdaQueryWrapper = new LambdaQueryWrapper();


        if(dto.getUAVId() != null){
            lambdaQueryWrapper.eq(Localization::getUAVId,dto.getUAVId());
        }

        if(dto.getImage() != null){
            lambdaQueryWrapper.eq(Localization::getImage,dto.getImage());
        }

        if(dto.getBeginDate() != null && dto.getEndDate() != null){
            lambdaQueryWrapper.between(Localization::getLocalizationTime,dto.getBeginDate(),dto.getEndDate());
        }

        if(dto.getBeginLongitude() != null && dto.getEndLongitude() !=null){
            lambdaQueryWrapper.between(Localization::getLongitude,dto.getBeginLongitude(),dto.getEndLongitude());
        }

        if(dto.getBeginLatitude() != null && dto.getEndLatitude() != null){
            lambdaQueryWrapper.between(Localization::getLatitude,dto.getBeginLatitude(),dto.getEndLatitude());
        }

        page  = page(page,lambdaQueryWrapper);

        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int) page.getTotal());
        responseResult.setData(page.getRecords());

        return responseResult;
    }


}
