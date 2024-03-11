package com.heima.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("wm_sensitive")
public class WmSensitive implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("sensitives")
    private String sensitives;

    @TableField("created_time")
    private Date createdTime;
}
