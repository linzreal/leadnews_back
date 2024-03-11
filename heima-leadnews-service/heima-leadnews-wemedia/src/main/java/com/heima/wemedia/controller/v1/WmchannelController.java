package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelSaveDto;
import com.heima.model.wemedia.dtos.WmChannelPageReqDto;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channel")
public class WmchannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public ResponseResult findAll(){
        return wmChannelService.findAll();
    }


    @PostMapping("/save")
    public ResponseResult save(@RequestBody ChannelSaveDto dto){
        return wmChannelService.save(dto);
    }

    @GetMapping("/del/{id}")
    public ResponseResult del(@PathVariable Integer id){
        return wmChannelService.deleteById(id);
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmChannelPageReqDto dto){
        return wmChannelService.pageList(dto);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody ChannelSaveDto dto){
        return wmChannelService.updateChannel(dto);
    }



}
