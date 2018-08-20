package com.demo.cameldemo.demo.动态终端转发;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * 动态选择一个结束端进行转发
 */
@Component
public class 动态查找终端 extends RouteBuilder {
    @Override
    public void configure() {
        // 根据post中body内容选择转发路由
        from("jetty:http://0.0.0.0:12457")
                .process(ex -> {
                    ex.getIn().setHeader("serviceRoute", ex.getIn().getBody(String.class));
                })
                .toD("direct:${header.serviceRoute}");

        from("direct:print")
                .process(exchange -> System.out.println("print"))
                .process(exchange -> {
                    System.out.println(exchange.getFromEndpoint().getEndpointKey() + exchange.getExchangeId());
                });

        from("direct:print1")
                .process(exchange -> System.out.println("print1"))
                .process(exchange -> System.out.println(exchange.getFromEndpoint().getEndpointKey() + exchange.getExchangeId()));

        from("direct:print2")
                .process(exchange -> System.out.println("print2"))
                .process(exchange -> System.out.println(exchange.getFromEndpoint().getEndpointUri() + exchange.getExchangeId()));
    }
}
