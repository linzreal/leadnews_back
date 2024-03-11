package com.heima.model.behavior.dtos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class LikesBehaviorDto {

    @IdEncrypt
    private Long articleId;

    private Integer operation;

    private Integer type;
}
