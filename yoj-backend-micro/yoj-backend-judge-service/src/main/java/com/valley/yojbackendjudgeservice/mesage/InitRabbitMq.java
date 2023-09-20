package com.valley.yojbackendjudgeservice.mesage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 初始化消息队列
 */
@Slf4j
public class InitRabbitMq {

    final private static String EXCHANGE_NAME = "judge_exchange";
    final private static String QUEUE_NAME = "judge_queue";
    final private static String ROUTING_KEY = "key";

    /**
     * 启动消息队列
     */
    public static void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            log.info("RabbitMQ启动成功");
        } catch (IOException | TimeoutException e) {
            log.error("初始RabbitMQ失败：", e);
        }
    }
}
