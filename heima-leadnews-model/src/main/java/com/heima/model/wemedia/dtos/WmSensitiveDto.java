package com.heima.model.wemedia.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class WmSensitiveDto {

    private Integer id;

    private String sensitives;

    private Date createdTime;
}
