package com.demo.cameldemo.type_convert;

import org.apache.camel.Exchange;

public class ExchangeHandler {
    public void say(Exchange exchange) {
        Object ss = exchange.getIn().getBody(String.class);
        System.out.println(ss);
    }
}
