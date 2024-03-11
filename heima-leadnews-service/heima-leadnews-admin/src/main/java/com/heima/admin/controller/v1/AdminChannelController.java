package com.heima.admin.controller.v1;

import com.heima.admin.service.AdChannelService;
import com.heima.model.admin.dtos.ChannelSaveDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ！！！！！已经废除
 */
@RestController
@RequestMapping("/api/v1/channel")
public class AdminChannelController {

    @Autowired
    private AdChannelService adChannelService;


    /**
     * 添加
     * @param saveDto
     * @return
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody ChannelSaveDto saveDto){
        return adChannelService.save(saveDto);
    }


    @GetMapping("/del/{id}")
    public ResponseResult del(@PathVariable Integer id){
        return adChannelService.delById(id);
    }


}
