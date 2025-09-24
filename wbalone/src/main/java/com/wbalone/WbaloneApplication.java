package com.wbalone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableWebSecurity
@MapperScan("com.wbalone.system.mapper")
public class WbaloneApplication {
    public static void main(String[] args) {
        SpringApplication.run(WbaloneApplication.class, args);
    }
}
