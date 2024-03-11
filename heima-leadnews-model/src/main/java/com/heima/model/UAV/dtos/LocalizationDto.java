package com.heima.model.UAV.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class LocalizationDto {

    private Long UAV_id;

    private String longitude;

    private String latitude;

    private String image;

    private Date localizationTime;


}
