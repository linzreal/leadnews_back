package com.heima.model.wemedia.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ChannelSaveDto {

    private Integer id;

    private String name;

    private String description;

    private Boolean isDefault;

    private Boolean status;

    private Integer ord;

    private Date createdTime;
}
