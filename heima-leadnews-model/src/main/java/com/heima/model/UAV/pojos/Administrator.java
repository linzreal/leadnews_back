package com.heima.model.UAV.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tab_administrator")
public class Administrator implements Serializable {
    @TableId(type = IdType.ID_WORKER)
    private Long administratorId;

    @TableField("phone")
    private String phone;

    @TableField("administrator_name")
    private String administratorName;

    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    @TableField("sex")
    private Integer sex;

    @TableField("image")
    private String image;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private Data createTime;


}
