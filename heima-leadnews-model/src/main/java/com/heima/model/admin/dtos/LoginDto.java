package com.heima.model.admin.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class LoginDto {

    private String name;

    private String password;
}
