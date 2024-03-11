package com.heima.model.article.dtos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class ArticleBehaviorDto {

    @IdEncrypt
    private Long articleId;

    private Integer authorId;

}
