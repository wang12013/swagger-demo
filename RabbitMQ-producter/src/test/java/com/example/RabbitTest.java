package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangzy
 * @date 2022/6/24 9:19
 */
@SpringBootTest
public class RabbitTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;



    @Test
    public void test(){
        //队列名
        String queueName = "hello_world";
        //消息内容
        String message = "这是一条消息";

        rabbitTemplate.convertAndSend(queueName,message);
    }

}
