package com.valley.yojbackendquestionservice.message;

import com.valley.yojbackendcommon.constant.RabbitMqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送判题消息
 */
@Component
public class MessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {
        rabbitTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_NAME, RabbitMqConstant.ROUTING_KEY, message);
    }
}
