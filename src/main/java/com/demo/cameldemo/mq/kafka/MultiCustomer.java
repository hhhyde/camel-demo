package com.demo.cameldemo.mq.kafka;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;

/**
 * 多客户端订阅同一个topic
 */
@Component
public class MultiCustomer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        KafkaComponent kafka = new KafkaComponent();
        kafka.setBrokers("119.27.161.183:9092");
        getContext().addComponent("kafka", kafka);
        // 多客户端全部读取
        from("timer://MultiCustomer?repeatCount=10")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息", String.class);
                    exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, 0;
                    exchange.getIn().setHeader(KafkaConstants.KEY, "1");
                }).to("kafka:119.27.161.183:9092?topic=MultiCustomer");

        from("kafka:119.27.161.183:9092?topic=MultiCustomer&groupId=testing1&autoOffsetReset=earliest&consumersCount=1")
                .process(exchange -> {
                    String messageKey = "";
                    if (exchange.getIn() != null) {
                        Message message = exchange.getIn();
                        Integer partitionId = (Integer) message
                                .getHeader(KafkaConstants.PARTITION);
                        String topicName = (String) message
                                .getHeader(KafkaConstants.TOPIC);
                        if (message.getHeader(KafkaConstants.KEY) != null)
                            messageKey = (String) message
                                    .getHeader(KafkaConstants.KEY);
                        Object data = message.getBody();


                        System.out.println("1topicName :: "
                                + topicName + " partitionId :: "
                                + partitionId + " messageKey :: "
                                + messageKey + " message :: "
                                + data);
                    }
                });
        from("kafka:119.27.161.183:9092?topic=MultiCustomer&groupId=testing1&autoOffsetReset=earliest&consumersCount=2")
                .process(exchange -> {
                    String messageKey = "";
                    if (exchange.getIn() != null) {
                        Message message = exchange.getIn();
                        Integer partitionId = (Integer) message
                                .getHeader(KafkaConstants.PARTITION);
                        String topicName = (String) message
                                .getHeader(KafkaConstants.TOPIC);
                        if (message.getHeader(KafkaConstants.KEY) != null)
                            messageKey = (String) message
                                    .getHeader(KafkaConstants.KEY);
                        Object data = message.getBody();


                        System.out.println("2topicName :: "
                                + topicName + " partitionId :: "
                                + partitionId + " messageKey :: "
                                + messageKey + " message :: "
                                + data);
                    }
                });


        // 多客户端其中选择一个读取
    }
}
