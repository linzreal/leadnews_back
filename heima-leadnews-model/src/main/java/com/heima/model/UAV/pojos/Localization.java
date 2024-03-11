package com.heima.model.UAV.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tab_localization")
public class Localization implements Serializable {
    @TableId(type = IdType.ID_WORKER)
    private Long localizationId;

    @TableField("UAV_id")
    private Long UAVId;

    @TableField("longitude")
    private Double longitude;

    @TableField("latitude")
    private Double latitude;

    @TableField("image")
    private String image;

    @TableField("localization_time")
    private Date localizationTime;

}
