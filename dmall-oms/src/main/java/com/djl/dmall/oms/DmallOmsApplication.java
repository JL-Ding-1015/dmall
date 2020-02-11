package com.djl.dmall.oms;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.djl.dmall.oms.mapper")
@EnableDubbo
@SpringBootApplication
public class DmallOmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DmallOmsApplication.class, args);
    }

}
