package com.shf.securitydemo01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan("com.shf.securitydemo01.mapper")
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityDemo01Application {

    public static void main(String[] args) {
        SpringApplication.run(SecurityDemo01Application.class, args);
    }

}
