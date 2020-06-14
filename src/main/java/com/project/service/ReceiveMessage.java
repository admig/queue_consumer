package com.project.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

@Service
public class ReceiveMessage {

    /**
     * 消费队列中的消息
     *
     * @param message
     * @param session
     */
    @JmsListener(destination = "${myqueue}", containerFactory = "jmsListenerContainerQueue")
    public void receiveQueue(TextMessage message, Session session) {
        try {
            // message.acknowledge();  // 签收
            System.out.println("---queue:" + message.getText());
            System.out.println("创建分支");
        } catch (JMSException e) {
            e.printStackTrace();
        }

        try {
            session.recover();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
