package com.demo.cameldemo.demo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;


public class 自动创建子服务 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jetty:http://0.0.0.0:12456")
                .process((exchange) -> {
//                    System.out.println(exchange.getIn().getBody(String.class));
                });


        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.process(ex-> System.out.println(ex.getIn().getBody()));
    }
}
