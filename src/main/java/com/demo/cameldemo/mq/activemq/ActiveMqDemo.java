package com.demo.cameldemo.mq.activemq;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class ActiveMqDemo extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setBrokerURL("tcp://127.0.0.1:61616");

        getContext().addComponent("activemq", activeMQComponent);

        deadLetterQueue();

    }


    /**
     * 消息共享
     */
    public void messageShare() {
        // 同一个queue的消费者,共享这个queue的消息
//        from("activemq:queue:foo")
//                .to("log:sample");
//        from("activemq:queue:foo")
//                .to("log:sample1");

        from("timer:bar?repeatCount=10&period=500")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
                })
                .to("activemq:queue:foo");
    }

    /**
     * 消息分发
     */
    public void messageDispatch() {
        // 同一个topic的消费者,共享这个topic的消息
        from("activemq:topic:foo")
                .to("log:sample");
        from("activemq:topic:foo")
                .to("log:sample1");

        from("timer:bar?repeatCount=10&period=500")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
                })
                .to("activemq:topic:foo");
    }

    /**
     * 死信队列
     * 手动重试,可以通过api将死信队列中的数据移动到指定队列(如原队列中重新消费)
     */
    void deadLetterQueue() {
        // 同一个topic的消费者,共享这个topic的消息

        errorHandler(
                // 重试之后进死信通道
                deadLetterChannel("activemq:queue:dead")
                        // 最大重试次数n,进行正常执行后追加的重试.最终执行次数1+n
                        .maximumRedeliveries(1)
                        // 重试延迟ms
                        .redeliveryDelay(1000)
        );

        from("activemq:queue:foo")
                .routeId("死信队列测试")
                .process(exchange -> {
                    // 重试期间才会产生
                    int redeliveryCounter = exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER, 0, Integer.class);
                    int redeliveryMaxCounter = exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER, 0, Integer.class);

                    long time = System.currentTimeMillis();
                    System.out.println(time+"|"+exchange.getIn().getBody() + "|已重试次数" + redeliveryCounter + "|最大重试次数" + redeliveryMaxCounter);
                    if (time % 10 % 2 == 1) {
                        // 最后一位是奇数
                        throw new RuntimeException("抛出异常.");
                    }
                    System.out.println("成功消费"+exchange.getIn().getBody());
                })
                .to("log:sample");

        from("timer:bar?repeatCount=10&period=1000")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
                })
                .to("activemq:queue:foo");
    }
}
