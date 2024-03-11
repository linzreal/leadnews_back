package com.heima.search.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface ArticleSearchService  {
    /**
     *
     * @param searchDto
     * @return
     */
    public ResponseResult search( UserSearchDto searchDto) throws IOException;
}
