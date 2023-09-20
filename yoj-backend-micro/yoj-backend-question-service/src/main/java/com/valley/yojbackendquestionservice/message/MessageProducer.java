package com.valley.yojbackendquestionservice.message;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送判题消息
 */
@Component
public class MessageProducer {

    final private static String EXCHANGE_NAME = "judge_exchange";
    final private static String ROUTING_KEY = "key";

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }
}
