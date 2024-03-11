package com.heima.model.UAV.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class UAVRegisterDto {

    private String name;

    private String phone;

    private String password;

    private String salt;

    private Integer sex;

    private String image;

    private Integer status;

    private Date create_time;
}
