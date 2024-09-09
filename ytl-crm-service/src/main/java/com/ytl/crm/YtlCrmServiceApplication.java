package com.ytl.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableScheduling
//@EnableAsync
@EnableCaching
@EnableFeignClients
@MapperScan(basePackages = {"com.ytl.crm.mapper"})
@ComponentScan(basePackages = {"com.ytl.crm"})
public class YtlCrmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtlCrmServiceApplication.class, args);
    }

}
