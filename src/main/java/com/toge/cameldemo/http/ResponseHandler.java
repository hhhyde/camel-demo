package com.toge.cameldemo.http;

import org.apache.camel.Exchange;

public class ResponseHandler {
    public void handler(Exchange exchange){
        System.out.println(exchange.getIn().getHeader(Exchange.HTTP_METHOD));
        String response = exchange.getIn().getBody(String.class);
        System.out.println(response);
    }
}
