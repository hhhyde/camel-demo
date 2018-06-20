package com.toge.cameldemo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyCamelRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("ftp://192.168.0.212:5555/?username=ff&password=ff&localWorkDirectory=/dmp123&delay=10000").to("file://D://ftp");
    }
}
