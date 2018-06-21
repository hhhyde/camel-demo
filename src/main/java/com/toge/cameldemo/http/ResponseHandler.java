package com.toge.cameldemo.http;

import org.apache.camel.Exchange;

public class ResponseHandler {
    public void handler(Exchange exchange){
        exchange.getOut().setBody(exchange.getIn().getBody());
    }
}
