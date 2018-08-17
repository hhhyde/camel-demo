package com.demo.cameldemo.mq;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RabbitDemo extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        System.out.println("开始rabbit---");
//        from("timer://my-timer?repeatCount=10")
//                .transform(constant("aaaaaaaa"))
//                .choice()
//                .when(exchange -> (new Random().nextInt()) < 5)
//                .to("rabbitmq://119.27.161.183:5672/camel-demo?username=guest&password=guest&routingKey=camel-1")
//                .otherwise()
//                .to("rabbitmq://119.27.161.183:5672/camel-demo?username=guest&password=guest&routingKey=camel-2");


        from("rabbitmq://119.27.161.183/camel-demo?username=guest&password=guest&routingKey=camel-1&autoAck=true")
                .process((ex) -> {
                    System.out.println("1" + ex.getIn().getBody(String.class));

                });

        from("rabbitmq://119.27.161.183/camel-demo?username=guest&password=guest&routingKey=camel-2&autoAck=false&publisherAcknowledgements=true")
                .process((ex) -> System.out.println("2" + ex.getIn().getBody(String.class)));
    }
}
