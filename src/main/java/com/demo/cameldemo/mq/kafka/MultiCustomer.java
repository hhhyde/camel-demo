package com.demo.cameldemo.mq.kafka;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.ErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.processor.DeadLetterChannel;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 多客户端订阅同一个topic
 */
//@Component
public class MultiCustomer extends RouteBuilder {

//    @Override
//    public void configure() throws Exception {
//        KafkaComponent kafka = new KafkaComponent();
//        kafka.setBrokers("119.27.161.183:9092");
//        getContext().addComponent("kafka", kafka);
//        // 多客户端全部读取
//        from("timer://MultiCustomer?repeatCount=10")
//                .process(exchange -> {
//                    exchange.getIn().setBody("测试消息", String.class);
//                    exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, 0);
//                    exchange.getIn().setHeader(KafkaConstants.KEY, "1");
//                }).to("kafka:119.27.161.183:9092?topic=MultiCustomer");
//
//        from("kafka:119.27.161.183:9092?topic=MultiCustomer&groupId=testing1&autoOffsetReset=earliest&consumersCount=1")
//                .log("1接收${header[kafka.TOPIC]}==${body}");
//        from("kafka:119.27.161.183:9092?topic=MultiCustomer&groupId=testing1&autoOffsetReset=earliest&consumersCount=2")
//                .log("2接收${header[kafka.TOPIC]}==${body}");
//        // 多客户端其中选择一个读取
//    }

    /**
     * 分发消息
     */
    @Override
    public void configure() {
//        消息分发();
        重发消息();
    }

    private void 消息分发() {
        KafkaComponent kafka = new KafkaComponent();
        kafka.setBrokers("119.27.161.183:9092");
        getContext().addComponent("kafka", kafka);
        // 多客户端全部读取, 如果再zookeeper中注册了一个组,
        from("kafka:119.27.161.183:9092?topic=MultiCustomer&groupId=clientGroup1")
                .routeId("分发终端1")
                .log("接收${header[kafka.TOPIC]}==${body}");
        from("kafka:119.27.161.183:9092?topic=MultiCustomer&groupId=clientGroup2")
                .routeId("分发终端2")
                .log("接收${header[kafka.TOPIC]}==${body}");

        from("timer://MultiCustomer?repeatCount=10")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息", String.class);
                    exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, 0);
                    exchange.getIn().setHeader(KafkaConstants.KEY, "1");
                })
                .to("kafka:119.27.161.183:9092?topic=MultiCustomer");
    }

    /**
     * 手动确认消息
     */
//    public void 手动确认消息() {
//        KafkaComponent kafka = new KafkaComponent();
//        kafka.setBrokers("119.27.161.183:9092");
//        getContext().addComponent("kafka", kafka);
//        // 多客户端全部读取, 如果再zookeeper中注册了一个组,
//        from("kafka:119.27.161.183:9092?topic=MultiCustomer&groupId=clientGroup1&autoCommitEnable=false&allowManualCommit=true")
//                .routeId("分发终端1")
//                .process(exchange -> {
//                    String body = exchange.getIn().getBody(String.class);
//                    KafkaManualCommit manual = exchange.getIn().getHeader(KafkaConstants.MANUAL_COMMIT, KafkaManualCommit.class);
//                    if (System.currentTimeMillis() % 3 == 1) {
//                        System.out.println("已确认");
//                        manual.commitSync();
//                    }else {
//
//                    }
//                })
//                .log("接收${header[kafka.TOPIC]}==${body}");
//
//        from("timer://MultiCustomer?repeatCount=10&period=500")
//                .process(exchange -> {
//                    exchange.getIn().setBody("测试消息"+ DateUtil.formatDate(new Date()), String.class);
//                    exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, 0);
//                    exchange.getIn().setHeader(KafkaConstants.KEY, "1");
//                }).to("kafka:119.27.161.183:9092?topic=MultiCustomer");
//    }

    /**
     * 重发
     */
    public void 重发消息(){
        errorHandler(deadLetterChannel("jms:queue:ErrorHandler")
                .useOriginalMessage()
                .maximumRedeliveries(1));

        from("jms:queue:ErrorHandler")
                .log("Sending Exception to MyErrorProcessor")
                .process(process->{
                    System.out.println(process.getIn().getBody());
                });

        from("timer://MultiCustomer?repeatCount=2")
                .routeId("Route1")
                .setHeader("route1Header").constant("changed")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息", String.class);
                    exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, 0);
                    exchange.getIn().setHeader(KafkaConstants.KEY, "1");
                })
                .log(LoggingLevel.DEBUG, "Route1Logger", "Inside Route 1")
                .throwException(new Exception("E_MYERROR_01"))
                .to("file:/E:/Target/Done");
    }

}
