package com.valley.yojbackendjudgeservice;

import com.binarywang.spring.starter.wxjava.mp.config.WxMpAutoConfiguration;
import com.valley.yojbackendjudgeservice.mesage.InitRabbitMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        WxMpAutoConfiguration.class
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.valley.yojbackendserviceclient.service")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.valley")
public class JudgeServiceApplication {

    public static void main(String[] args) {
        InitRabbitMq.init();
        SpringApplication.run(JudgeServiceApplication.class, args);
    }

}
