package com.demo.cameldemo.mq.activemq;

import com.alibaba.fastjson.JSONArray;
import org.apache.activemq.ActiveMQQueueBrowser;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Optional;

//@Component
public class 代码直连 {

    @Autowired
    JmsTemplate jmsTemplate;

    @JmsListener(destination = "dead")
    public void 死信监听(Message message) {
        if (message instanceof TextMessage) {
            jmsTemplate.browse("dead", new BrowserCallback() {
                @Override
                public Object doInJms(Session session, QueueBrowser browser) throws JMSException {
                    if (browser instanceof ActiveMQQueueBrowser){
                        ActiveMQQueueBrowser activeMQQueueBrowser = (ActiveMQQueueBrowser) browser;
                        Queue queue = activeMQQueueBrowser.getQueue();
                        while (activeMQQueueBrowser.hasMoreElements()){
                            Object element = activeMQQueueBrowser.nextElement();
                            if (element instanceof ActiveMQTextMessage) {
                                ActiveMQTextMessage message1 = (ActiveMQTextMessage) element;
                                System.out.println(message1.getSize());
                                System.out.println(message1.getMessageId());
                            }

                        }
                        Object ggg = activeMQQueueBrowser.nextElement();

                        if (queue instanceof ActiveMQQueue){
                            ActiveMQQueue activeMQQueue = (ActiveMQQueue) queue;
                            System.out.println(444);
                        }
                        System.out.println(111);
                    }
                    browser.getEnumeration();
                    browser.getQueue();

                    System.out.println(123);
                    return null;
                }
            });
            System.out.println("#######" + message);
        }
    }

    /**
     * 获取所有dead队列里面的消息
     *
     */
    public String all() {
        jmsTemplate.setReceiveTimeout(1000);
        String list = jmsTemplate.browse("dead", (session, browser) -> {
            JSONArray list1 = new JSONArray();
            if (browser instanceof ActiveMQQueueBrowser) {
                ActiveMQQueueBrowser activeMQQueueBrowser = (ActiveMQQueueBrowser) browser;
                int i = 0;
                while (activeMQQueueBrowser.hasMoreElements()) {
                    i++;
                    Object element = activeMQQueueBrowser.nextElement();
                    if (element instanceof ActiveMQTextMessage) {
                        ActiveMQTextMessage message1 = (ActiveMQTextMessage) element;
                        list1.add(message1.getMessageId().toString());
                    }
                    // TODO: 2019-02-27 去掉3的限制
                    if (i == 3) {
                        break;
                    }
                }
            }
            return list1.toJSONString();
        });
        return list;
    }

    /**
     * 重发
     * @param oriQueueName
     * @param msgId
     * @param desQueueName
     */
    public void retry(String oriQueueName, String msgId, String desQueueName) {
        // 超时时间1秒
        jmsTemplate.setReceiveTimeout(1000);
        Optional<Message> message = Optional.ofNullable(jmsTemplate.receiveSelected(oriQueueName, String.format("JMSMessageID='%s'", msgId)));
        message.ifPresent(msg -> jmsTemplate.convertAndSend(desQueueName, msg));
    }

}
