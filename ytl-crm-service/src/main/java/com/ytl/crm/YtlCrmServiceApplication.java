package com.ytl.crm;

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
@ComponentScan(basePackages = {"com.ytl.crm"})
public class YtlCrmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtlCrmServiceApplication.class, args);
    }

}
