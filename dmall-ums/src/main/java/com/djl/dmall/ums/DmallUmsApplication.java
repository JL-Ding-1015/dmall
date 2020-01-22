package com.djl.dmall.ums;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan(basePackages = "com.djl.dmall.ums.mapper")
@SpringBootApplication
public class DmallUmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmallUmsApplication.class, args);
    }

}
