package com.heima.UAV.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.UAV.dtos.UAVLoginDto;
import com.heima.model.UAV.dtos.UAVRegisterDto;
import com.heima.model.UAV.pojos.Administrator;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface AdministratorService extends IService<Administrator> {

    /**
     *
     */
    public ResponseResult login(UAVLoginDto dto);

    public ResponseResult register(UAVRegisterDto dto);
}
