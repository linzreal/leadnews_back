package com.heima.model.article.vos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class BehaviorMessageVo {

    @IdEncrypt
    private Long articleId;


    /**
     * 0 点赞， 1收藏，2评论，3观看
     */

    public enum OpinionType {
        LIKE(0),   // 点赞
        COLLECT(1), // 收藏
        COMMENT(2), // 评论
        VIEW(3)    // 观看
        ;

        OpinionType(int i) {
        }
    }

    private OpinionType opinionType;

    /**
     * 0确认， 1取消
     */
    private Integer type;


}
