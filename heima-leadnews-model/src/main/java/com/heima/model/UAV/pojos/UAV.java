package com.heima.model.UAV.pojos;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tab_UAV")
public class UAV implements Serializable {

    @TableId(type = IdType.ID_WORKER)
    private Long UAVId;

    @TableField("administrator_id")
    private Long administratorId;

    @TableField("type")
    private String type;

    @TableField("camera")
    private String camera;

    @TableField("height")
    private Long height;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private Date createTime;

}
