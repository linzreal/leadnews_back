package com.heima.model.UAV.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.util.Date;

@Data
public class LocalizationPageReqDto extends PageRequestDto {

    private String UAVId;

    private String image;

    private Date beginDate;

    private Date endDate;

    private Double beginLongitude;

    private Double endLongitude;

    private Double beginLatitude;

    private Double endLatitude;

}
