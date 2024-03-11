package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.dtos.WmSensitivePageReqDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    @Override
    public ResponseResult save(WmSensitiveDto dto) {

        if(dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmSensitive wmSensitive = new WmSensitive();

        BeanUtils.copyProperties(dto,wmSensitive);
        wmSensitive.setCreatedTime(new Date());

        wmSensitiveMapper.insert(wmSensitive);

        return ResponseResult.okResult(wmSensitive);

    }

    @Override
    public ResponseResult list(WmSensitivePageReqDto dto) {
        dto.checkParam();

        IPage page = new Page(dto.getPage(),dto.getSize());

        LambdaQueryWrapper<WmSensitive> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if(StringUtils.isNotBlank(dto.getName())){
            lambdaQueryWrapper.eq(WmSensitive::getSensitives,dto.getName());
        }

        page = page(page,lambdaQueryWrapper);

        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        responseResult.setData(page.getRecords());

        return responseResult;


    }

    @Override
    public ResponseResult del(Integer id) {
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        wmSensitiveMapper.deleteById(id);

        return ResponseResult.okResult(id);

    }

    @Override
    public ResponseResult updateSensitive(WmSensitiveDto dto) {

        if(dto == null || dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        WmSensitive wmSensitive = new WmSensitive();

        BeanUtils.copyProperties(dto,wmSensitive);

        wmSensitiveMapper.updateById(wmSensitive);

        return ResponseResult.okResult(wmSensitive);
    }
}
