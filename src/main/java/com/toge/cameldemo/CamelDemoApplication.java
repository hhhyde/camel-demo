package com.toge.cameldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:my-camel.xml")
public class CamelDemoApplication {
    // TODO: 2018-06-20 ws组件
    // TODO: 2018-06-20 http 组件待实现 
    public static void main(String[] args) {
        SpringApplication.run(CamelDemoApplication.class, args);
    }
}
