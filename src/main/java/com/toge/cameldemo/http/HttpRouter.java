package com.toge.cameldemo.http;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class HttpRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        httpRoute();
    }


    private void httpRoute(){
        from("timer://aaa?repeatCount=1")
                .process(new BodyProcess())
                .to("http://localhost:8080/test/echo")
                .bean(new ResponseHandler(),"handler");
    }
}
