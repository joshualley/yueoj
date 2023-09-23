package com.valley.yojbackendcodesandbox;

import com.binarywang.spring.starter.wxjava.mp.config.WxMpAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        WxMpAutoConfiguration.class,
        RedisAutoConfiguration.class,
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.valley.yojbackendserviceclient.service")
@ComponentScan("com.valley")
public class CodeSandboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeSandboxApplication.class, args);
    }

}
