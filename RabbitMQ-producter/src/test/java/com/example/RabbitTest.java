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
        rabbitTemplate.convertAndSend(queueName,message);
        rabbitTemplate.convertAndSend(queueName,message);
        rabbitTemplate.convertAndSend(queueName,message);
        //convertAndSend能自动将
    }

    @Test
    public void test2(){
        User user = new User("小王王", "16", 180.5);
        //队列名
        String queueName = "hello_world";

        //convertAndSend，会自动将传的消息Object转换为一个Message，
        //并且这个对象的类实现了Serializable接口才能直接发对象作为消息，不然会报错
        //SimpleMessageConverter only supports String, byte[] and Serializable payloads, received: com.example.User
        //user类implements Serializable后能解决这个报错

        //发三条消息
        rabbitTemplate.convertAndSend(queueName,user);
        rabbitTemplate.convertAndSend(queueName,user);
        rabbitTemplate.convertAndSend(queueName,user);
    }
}
