package com.djl.dmall.pms;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * 事务的最终解决方案：
 *  1、普通加事务，倒入jdbc-starter，开启@EnableTransactionManagement
 *      之后在需要加事务的方法上添加@Transactional
 *  2、方法自己调用自己类的方法，按上述步骤加不上事务
 *      1）、倒入aop包，开启代理对象的相关功能
 *      2）、获取当前类的代理对象，在通过 对象.方法 调用方法即可
 *           其中，@EnableAspectAutoProxy(exposeProxy = true)：暴露代理对象
 *                  获取代理对象，通过代理对象调用方法
 *
 */
@EnableDubbo
@MapperScan(basePackages = {"com.djl.dmall.pms.mapper"})
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class DmallPmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmallPmsApplication.class, args);
    }

}
