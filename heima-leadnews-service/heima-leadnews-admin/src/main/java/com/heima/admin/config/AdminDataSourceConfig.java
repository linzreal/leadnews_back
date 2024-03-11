package com.heima.admin.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(basePackages = AdminDataSourceConfig.PACKAGE,sqlSessionFactoryRef = "adminSqlSessionFactory")
public class AdminDataSourceConfig {

    final static String PACKAGE = "com.heima.admin.mapper.adminMapper";
    final static String MAPPER_LOCATION = "classpath:mapper/adminMapper/*.xml";

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.adminDb.url}")
    private String url;

    @Value("${spring.datasource.adminDb.username}")
    private String userName;

    @Value("${spring.datasource.adminDb.password}")
    private String password;
    @Bean(name = "adminDataSource")
    @Primary
    public DataSource adminDataSource(){

        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "adminTransactionManager")
    @Primary
    public DataSourceTransactionManager adminTransactionManager(){
        return new DataSourceTransactionManager(adminDataSource());
    }

    @Bean(name ="adminSqlSessionFactory" )
    @Primary
    public SqlSessionFactory adminSqlSessionFactory(@Qualifier("adminDataSource") DataSource adminDataSource
    ) throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(adminDataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(AdminDataSourceConfig.MAPPER_LOCATION));

        return sessionFactoryBean.getObject();
    }




}
