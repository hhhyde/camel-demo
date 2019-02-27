package com.demo.cameldemo.mq.activemq;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class 消息过滤 extends RouteBuilder {
    @Override
    public void configure() throws Exception {


        from("activemq:queue:dead").process(exchange -> {
            System.out.println(exchange.getIn().getBody());
            exchange.getOut().setHeader("routeId",exchange.getIn().getHeader("OriginRouteId")); // 指定发给2号
        })
        .to("activemq:topic:重复消费-消息分发");

//        selector();
    }

    /**
     * 参看 https://timjansen.github.io/jarfiller/guide/jms/selectors.xhtml
     * 1. selector里面的值严格区分字母数字
     */
    void selector(){
        from("timer:消息过滤?repeatCount=2&period=500")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
//                    exchange.getOut().setHeader("routeId",1);
                    exchange.getOut().setHeader("routeId",2); // 指定发给2号
                })
                .to("activemq:topic:foo3");

//         SQL92 规范, (routeId=1) OR (routeId IS NULL)
        from("activemq:topic:foo3?selector=%28routeId%3D1%29+OR+%28routeId+IS+NULL%29")
                .process(exchange -> {
                    System.out.println(exchange.getIn().getBody());
                })
                .log("log:1");
//        (routeId=2) OR (routeId IS NULL)
        from("activemq:topic:foo3?selector=%28routeId%3D2%29+OR+%28routeId+IS+NULL%29")
                .log("log:2");
    }
}
