package com.heima.model.user.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class LoginDto {

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号",required = true)
    private String phone;


    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
