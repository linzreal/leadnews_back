package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.common.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jcodings.util.Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
    /**
     * app端登陆功能
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {
        //正常登陆
        if(StringUtils.isNotBlank(dto.getPhone())&&StringUtils.isNotBlank(dto.getPassword())){

            //mybatis-plus用法，由于ApUser类已经加了@TableName注解，因此可以这样用
            ApUser dbUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone,dto.getPhone()));

            if(dbUser==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在");
            }

            //验证密码
            String salt = dbUser.getSalt();

            String password  = dto.getPassword();
            String passwordEncoded = DigestUtils.md5DigestAsHex((password+salt).getBytes());
            if(!dbUser.getPassword().equals(passwordEncoded)){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"密码错误");
            }

            //生成token并返回
            String token = AppJwtUtil.getToken(dbUser.getId().longValue());

            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            dbUser.setPassword("");
            dbUser.setSalt("");
            map.put("user",dbUser);

            return ResponseResult.okResult(map);



        }else{
            //游客登陆
            String token = AppJwtUtil.getToken(0L);
            Map<String,Object>map = new HashMap<>();
            map.put("token",token);

            return ResponseResult.okResult(map);
        }

    }
}
