package com.example.Listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wangzy
 * @date 2022/6/24 11:04
 */
@Component
public class MyRabbitListener {

    /**
     * 监听某个队列的消息
     * @param message 接收到的消息
     */
    @RabbitListener(queues = "${queName}") //指定的队列名
    public void myListener1(String message){
        System.out.println("消费者接收到的消息为：" + message);
    }

}
