package com.demo.cameldemo.demo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class 多入口 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jetty:http://0.0.0.0:12458")
                .from("activemq:queue:ssg")
                .from("jetty:http://0.0.0.0:12459")
                .log("${body}");
    }
}
