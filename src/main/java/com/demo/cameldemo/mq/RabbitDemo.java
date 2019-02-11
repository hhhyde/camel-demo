package com.demo.cameldemo.mq;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.direct.DirectComponent;
import org.apache.camel.component.direct.DirectEndpoint;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.component.ref.RefComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RabbitDemo extends RouteBuilder {

    @Autowired
    CamelContext camelContext;


    @Override
    public void configure() throws Exception {

        System.out.println("开始rabbit---");
        RabbitDemo demo = new RabbitDemo();
        AtomicReference<String> direct = new AtomicReference<>("1");

        getContext().getEndpoint("dd");

        from("timer://my-timer?repeatCount=10")
//                .process(ex -> {
//                    Endpoint endpoint = new RefEndpoint("ref:dircect1",new RefComponent());
//                    getContext().addEndpoint("ref:direct1", endpoint);
//                });
                .process(ex -> {
                    direct.set("2");
                    System.out.println(direct);
                })
                .to("seda:" + direct);


        from("direct:1")
                .process(ex -> {
                    System.out.println("direct1=" + ex.getExchangeId());
                });

        from("seda:1")
                .process(ex -> {
                    System.out.println("direct2=" + ex.getExchangeId());
                });


        getContext().addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("servlet:abb").log("666");
            }
        });
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("servlet:acc").log("777");
            }
        });
        camelContext.start();
        List<Route> ss = camelContext.getRoutes();
        System.out.println(ss);


//        from("rabbitmq://119.27.161.183/camel-demo?username=guest&password=guest&routingKey=camel-1&autoAck=true")
//                .process((ex) -> {
//                    System.out.println("1" + ex.getIn().getBody(String.class));
//                });
//
//        from("rabbitmq://119.27.161.183/camel-demo?username=guest&password=guest&routingKey=camel-2&autoAck=false&publisherAcknowledgements=true")
//                .process((ex) -> System.out.println("2" + ex.getIn().getBody(String.class)));
    }

    void 直接通过URI发送数据() {
        from("timer://my-timer?repeatCount=10")
                .transform(constant("aaaaaaaa"))
                .setHeader(RabbitMQConstants.ROUTING_KEY, constant("lll"))
                .choice()
                .when(exchange -> (new Random().nextInt()) < 5)
                .to("rabbitmq://119.27.161.183:5672/camel-demo?username=guest&password=guest")
                .otherwise()
                .to("rabbitmq://119.27.161.183:5672/camel-demo?username=guest&password=guest");
    }
}


class customer implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Endpoint endpoint = new DirectEndpoint("direct1", new DirectComponent());
        exchange.getContext().addEndpoint("", endpoint);
    }
}

class ggg extends RefComponent {

}
