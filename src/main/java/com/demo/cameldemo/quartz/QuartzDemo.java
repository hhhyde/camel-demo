package com.demo.cameldemo.quartz;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class QuartzDemo extends RouteBuilder {
    @Override
    public void configure() {

    from("quartz://0 0/1 0/1 * * ?")
            .log("444");
    }

}
