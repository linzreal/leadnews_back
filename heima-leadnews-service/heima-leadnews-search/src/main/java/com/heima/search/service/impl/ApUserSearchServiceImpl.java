package com.heima.search.service.impl;

import com.heima.search.pojos.ApUserSearch;
import com.heima.search.service.ApUserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ApUserSearchServiceImpl implements ApUserSearchService {


    /**
     * 保存用户搜索历史记录
     *
     * @param keyword
     * @param userId
     */
    @Override
    @Async
    public void insert(String keyword, Integer userId) {
        return;
    }
}
