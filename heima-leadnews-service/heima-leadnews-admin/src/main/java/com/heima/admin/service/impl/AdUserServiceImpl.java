package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.adminMapper.AdUserMapper;
import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.LoginDto;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;


@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {
    @Override
    public ResponseResult login(LoginDto dto) {
        if(dto.getName() == null || dto.getPassword() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        AdUser dbUser = getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName,dto.getName()));

        if(dbUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        String salt = dbUser.getSalt();
        String pswd = dto.getPassword();


        pswd = DigestUtils.md5DigestAsHex((pswd + salt).getBytes());
        if(pswd.equals(dbUser.getPassword())){
            //4.返回数据  jwt
            Map<String,Object> map  = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(dbUser.getId().longValue()));
            dbUser.setSalt("");
            dbUser.setPassword("");
            map.put("user",dbUser);
            return ResponseResult.okResult(map);

        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

    }
}
