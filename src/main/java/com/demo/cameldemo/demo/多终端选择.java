package com.demo.cameldemo.demo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class 多终端选择 extends RouteBuilder {
    @Override
    public void configure() {

        from("timer://my-timer?repeatCount=1")
                .transform(constant("DummyBody"))
                .choice()
                .when(body().contains("DummyBody1"))
                .to("direct:print")
                .when(body().contains("echo"))
                .to("direct:print1")
                .otherwise()
                .to("direct:print2");

        from("direct:print")
                .process(exchange -> System.out.println("print"))
                .process(exchange -> System.out.println(exchange.getFromEndpoint().getEndpointKey() + exchange.getExchangeId()));

        from("direct:print1")
                .process(exchange -> System.out.println("print1"))
                .process(exchange -> System.out.println(exchange.getFromEndpoint().getEndpointKey() + exchange.getExchangeId()));

        from("direct:print2")
                .process(exchange -> System.out.println("print2"))
                .process(exchange -> System.out.println(exchange.getFromEndpoint().getEndpointUri() + exchange.getExchangeId()));
    }
}
