package com.xfl.boot.common.utils.ramq;

import com.xfl.boot.entity.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by XFL
 * time on 2018/5/15 22:33
 * description:
 */
@Component
public class HelloSender {
    @Autowired
    private AmqpTemplate template;

    public void send() {
        User user = new User();    //实现Serializable接口
        user.setDesc("测试");
        user.setName("xfl");
        user.setAge("22");
        template.convertAndSend("queue", user);
    }

    public void sendTopic() {
        template.convertAndSend("exchange", "topic.message", "hello,rabbit");
    }

    public void sendFanout() {
        template.convertAndSend("fanoutExchange", "", "xixixi,hahahahha");
    }
}
