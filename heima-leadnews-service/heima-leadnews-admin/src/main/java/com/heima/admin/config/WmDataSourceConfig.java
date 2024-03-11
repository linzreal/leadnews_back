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

@Configuration
@MapperScan(basePackages = WmDataSourceConfig.PACKAGE,sqlSessionFactoryRef = "wmSqlSessionFactory")
public class WmDataSourceConfig {

    final static String PACKAGE = "com.heima.admin.mapper.wmMapper";

    final static  String MAPPER_LOCATION = "classpath:mapper/wmMapper/*.xml";

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.wmDb.url}")
    private String url;


    @Value("${spring.datasource.wmDb.username}")
    private String userName;



    @Value("${spring.datasource.wmDb.password}")
    private String password;


    @Bean(name = "wmDataSource")
    public DataSource wmDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "wmTransactionManager")
    public DataSourceTransactionManager wmTransactionManager(){
        return new DataSourceTransactionManager(wmDataSource());
    }

    @Bean(name ="wmSqlSessionFactory" )
    public SqlSessionFactory wmSqlSessionFactory(@Qualifier("wmDataSource") DataSource wmDataSource
    ) throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(wmDataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(WmDataSourceConfig.MAPPER_LOCATION));

        return sessionFactoryBean.getObject();
    }



}
