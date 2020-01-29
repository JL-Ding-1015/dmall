package com.djl.dmall.sms;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan(basePackages = {"com.djl.dmall.sms.mapper"})
@SpringBootApplication
public class DmallSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmallSmsApplication.class, args);
    }

}
