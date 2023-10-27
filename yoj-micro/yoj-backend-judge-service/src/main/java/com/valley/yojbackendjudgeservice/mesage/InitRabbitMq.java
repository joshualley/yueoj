package com.valley.yojbackendjudgeservice.mesage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.valley.yojbackendcommon.constant.RabbitMqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 初始化消息队列
 */
@Slf4j
@Component
public class InitRabbitMq {

    @Value("${spring.rabbitmq.host:localhost}")
    private String hostIP;

    /**
     * 启动消息队列
     */
    @PostConstruct
    public void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(hostIP);
            factory.setPort(5672);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(RabbitMqConstant.EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            channel.queueDeclare(RabbitMqConstant.QUEUE_NAME, true, false, false, null);
            channel.queueBind(RabbitMqConstant.QUEUE_NAME, RabbitMqConstant.EXCHANGE_NAME, RabbitMqConstant.ROUTING_KEY);

            log.info("RabbitMQ启动成功: {}:5672", hostIP);
        } catch (IOException | TimeoutException e) {
            log.error("初始RabbitMQ失败：", e);
        }
    }
}
