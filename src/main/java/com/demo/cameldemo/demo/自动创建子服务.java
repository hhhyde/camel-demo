package com.demo.cameldemo.demo;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.stereotype.Component;

@Component
public class 自动创建子服务 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jetty:http://0.0.0.0:12456")
                .multicast()
                .process(new subProcess());
    }

}

class subProcess implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.process(ex -> System.out.println(ex.getIn().getBody()));

        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer://my-timer?repeatCount=1")
                        .process(ex -> System.out.println(ex.getExchangeId()));
            }
        };

        exchange.getContext().addRoutes(routeBuilder);
        exchange.getContext().start();
    }
}