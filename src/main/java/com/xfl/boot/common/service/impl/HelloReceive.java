package com.xfl.boot.common.service.impl;

import com.xfl.boot.entity.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by XFL
 * time on 2018/5/15 22:39
 * description:
 */
@Component
public class HelloReceive {

    @RabbitListener(queues = "queue")    //监听器监听指定的Queue
    public void processC(User user) {
        System.out.println("messages:" + user);
    }

    @RabbitListener(queues = "topic.message")    //监听器监听指定的Queue
    public void process1(String str) {
        System.out.println("message:" + str);
    }

    @RabbitListener(queues = "topic.messages")    //监听器监听指定的Queue
    public void process2(String str) {
        System.out.println("messages:" + str);
    }

    @RabbitListener(queues = "fanout.A")
    public void processA(String str1) {
        System.out.println("ReceiveA:" + str1);
    }

    @RabbitListener(queues = "fanout.B")
    public void processB(String str) {
        System.out.println("ReceiveB:" + str);
    }

    @RabbitListener(queues = "fanout.C")
    public void processC(String str) {
        System.out.println("ReceiveC:" + str);
    }

}
