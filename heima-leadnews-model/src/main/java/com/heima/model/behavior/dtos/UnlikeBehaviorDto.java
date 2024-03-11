package com.heima.model.behavior.dtos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class UnlikeBehaviorDto {
    @IdEncrypt
    private Long articleId;

    private Integer type;
}
