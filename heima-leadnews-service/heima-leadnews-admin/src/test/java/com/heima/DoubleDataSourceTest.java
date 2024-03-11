package com.heima;

import com.heima.admin.mapper.adminMapper.AdUserMapper;
import com.heima.admin.mapper.wmMapper.ChannelMapper;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.wemedia.pojos.WmChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = AdminApplication.class)
@RunWith(SpringRunner.class)
public class DoubleDataSourceTest {

    @Autowired
    private AdUserMapper adUserMapper;

    @Autowired
    private ChannelMapper channelMapper;


    @Test
    public void test(){
        AdUser adUser = adUserMapper.selectById(1);

        System.out.println(adUser);

        WmChannel wmChannel = channelMapper.selectById(1);

        System.out.println(wmChannel);


    }
}
