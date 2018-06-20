package com.toge.cameldemo;

import org.apache.camel.Exchange;

public class MyBean {
    public void say(Exchange exchange){
        System.out.println(exchange);
        System.out.println("123456789");
    }
}
