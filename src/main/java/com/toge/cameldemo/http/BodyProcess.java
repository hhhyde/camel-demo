package com.toge.cameldemo.http;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class BodyProcess implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(Exchange.HTTP_QUERY,"echo=echo&echo123=echo123");
        System.out.println(exchange);
    }
}
