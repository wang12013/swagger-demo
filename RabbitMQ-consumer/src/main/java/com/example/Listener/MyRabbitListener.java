package com.example.Listener;

import com.alibaba.fastjson.JSONObject;
import com.example.pojo.User;
import com.rabbitmq.client.Channel;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author wangzy
 * @date 2022/6/24 11:04
 */
@Component
public class MyRabbitListener {

    /**
     * 监听某个队列的消息,收到消息就签收
     * @param message 接收到的消息

    @RabbitListener(queues = "${queName}") //指定的队列名
    public void myListener1(String message){
        System.out.println("消费者接收到的消息为：" + message);
    }*/


    //消息的手动签收
    @RabbitListener(queues="hello_world")//监听队列
    public void handler1(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody(), "UTF-8");
        String messageId = message.getMessageProperties().getMessageId();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        //把JSONObject对象转成真正的对象类型

        try {
            //消息确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("收到消息，消费消息：" + jsonObject);

            //故意不签收，
            int i=1/0;

        }catch (Exception e){
            //失败后消息被确认,
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            // channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);失败后消息重新放回队列
            // 丢弃该消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            System.out.println("签收失败:" + jsonObject);
        }
    }

}
