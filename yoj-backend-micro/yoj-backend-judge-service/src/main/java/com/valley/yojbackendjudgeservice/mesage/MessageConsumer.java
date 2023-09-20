package com.valley.yojbackendjudgeservice.mesage;

import com.rabbitmq.client.Channel;
import com.valley.yojbackendjudgeservice.service.JudgeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 消息消费者
 */
@Component
@Slf4j
public class MessageConsumer {

    @Resource
    private JudgeService judgeService;

    @SneakyThrows
    @RabbitListener(queues = { "judge_queue" }, ackMode = "MANUAL")
    public void receive(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("received message: {}", message);
        if (StringUtils.isBlank(message)) {
            return;
        }
        long questionSubmitId = Long.parseLong(message);
        try {
            // 处理判题
            judgeService.doJudge(questionSubmitId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 处理失败后重新处理
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
