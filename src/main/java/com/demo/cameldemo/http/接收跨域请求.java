package com.demo.cameldemo.http;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMessage;
import org.springframework.stereotype.Component;

@Component
public class 接收跨域请求 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jetty:http://0.0.0.0:3333/camel/sss")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        if (HttpMessage.class.isInstance(exchange.getIn())) {
                            exchange.getOut().setHeader("Access-Control-Allow-Origin", "*");
                            exchange.getOut().setHeader("Access-Control-Allow-Credentials", "true");
                            exchange.getOut().setHeader("Access-Control-Allow-Methods", "*");
                            exchange.getOut().setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
                            exchange.getOut().setHeader("Access-Control-Expose-Headers", "*");
                        }
                        exchange.getOut().setBody("555");
                    }
                });
    }
}
