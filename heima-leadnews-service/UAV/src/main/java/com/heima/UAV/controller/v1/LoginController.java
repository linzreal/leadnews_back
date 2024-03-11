package com.heima.UAV.controller.v1;

import com.heima.UAV.service.AdministratorService;
import com.heima.model.UAV.dtos.UAVLoginDto;
import com.heima.model.UAV.dtos.UAVRegisterDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/UAV/login")
public class LoginController {

    @Autowired
    private AdministratorService administratorService;

    @PostMapping("/in")
    public ResponseResult login(@RequestBody UAVLoginDto dto){
        return administratorService.login(dto);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody UAVRegisterDto dto){
        return administratorService.register(dto);
    }

}
