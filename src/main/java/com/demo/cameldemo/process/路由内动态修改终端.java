package com.demo.cameldemo.process;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class 路由内动态修改终端 extends RouteBuilder {
    // TODO: 2019-02-24 未实现
    @Override
    public void configure() throws Exception {
        from("timer:nnnn?repeatCount=1&period=500")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
                    RouteDefinition routeDefinition = exchange.getContext().getRouteDefinition(exchange.getFromRouteId());
                    routeDefinition.to("direct:3");
                    exchange.getContext().addRouteDefinition(routeDefinition);
                })
                .to("direct:1")
                .to("direct:2");


        from("direct:1").to("log:1");
        from("direct:2").to("log:2");
        from("direct:3").to("log:3");
    }
}
