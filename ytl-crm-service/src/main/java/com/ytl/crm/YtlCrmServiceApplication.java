package com.ytl.crm;

import com.ytl.crm.config.DruidDataSourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
//@EnableAsync
//@Import(DruidDataSourceConfig.class)
@EnableCaching
@EnableFeignClients
@MapperScan(basePackages = {"com.ytl.crm.mapper"})
@ComponentScan(basePackages = {"com.ytl.crm"})
public class YtlCrmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtlCrmServiceApplication.class, args);
    }

}
