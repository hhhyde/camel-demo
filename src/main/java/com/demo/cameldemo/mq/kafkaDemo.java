package com.demo.cameldemo.mq;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Processor;

@Component
public class kafkaDemo extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer://my-timer?repeatCount=2")
                .setBody(constant("Message from Camel"))          // Message to send
                .setHeader(KafkaConstants.KEY, constant("Camel")) // Key of the message
                .to("kafka:test?brokers=119.27.161.183:9092");

        from("kafka:test?groupId=A&brokers=119.27.161.183:9092")
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .log("    on the partition ${headers[kafka.PARTITION]}")
                .log("    with the offset ${headers[kafka.OFFSET]}")
                .log("    with the key ${headers[kafka.KEY]}");
    }
}
