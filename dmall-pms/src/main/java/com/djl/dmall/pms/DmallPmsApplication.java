package com.djl.dmall.pms;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan(basePackages = {"com.djl.dmall.pms.mapper"})
@SpringBootApplication
public class DmallPmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmallPmsApplication.class, args);
    }

}
