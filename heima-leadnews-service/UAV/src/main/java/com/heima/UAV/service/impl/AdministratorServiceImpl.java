package com.heima.UAV.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.UAV.mapper.AdministratorMapper;
import com.heima.UAV.service.AdministratorService;
import com.heima.model.UAV.dtos.UAVLoginDto;
import com.heima.model.UAV.dtos.UAVRegisterDto;
import com.heima.model.UAV.pojos.Administrator;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.common.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class AdministratorServiceImpl extends ServiceImpl<AdministratorMapper, Administrator> implements AdministratorService {

    @Autowired
    private AdministratorMapper administratorMapper;
    @Override
    public ResponseResult login(UAVLoginDto dto) {

        //正常登陆
        if(StringUtils.isNotBlank(dto.getPhone())&&StringUtils.isNotBlank(dto.getPassword())){

            //mybatis-plus用法，由于ApUser类已经加了@TableName注解，因此可以这样用

            Administrator administrator = getOne(Wrappers.<Administrator>lambdaQuery().eq(Administrator::getPhone,dto.getPhone()));

            if(administrator==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"管理员信息不存在");
            }

            //验证密码
            String salt = administrator.getSalt();

            String password  = dto.getPassword();
            String passwordEncoded = DigestUtils.md5DigestAsHex((password+salt).getBytes());
            if(!administrator.getPassword().equals(passwordEncoded)){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"密码错误");
            }

            //生成token并返回
            String token = AppJwtUtil.getToken(administrator.getAdministratorId().longValue());

            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            administrator.setPassword("");
            administrator.setSalt("");
            map.put("user",administrator);

            return ResponseResult.okResult(map);



        }else{
            //游客登陆
            String token = AppJwtUtil.getToken(0L);
            Map<String,Object>map = new HashMap<>();
            map.put("token",token);

            return ResponseResult.okResult(map);
        }
    }

    @Override
    public ResponseResult register(UAVRegisterDto dto) {


        Administrator administrator = new Administrator();
        BeanUtils.copyProperties(dto,administrator);

        administratorMapper.insert(administrator);
        return ResponseResult.okResult(administrator);

    }


}
