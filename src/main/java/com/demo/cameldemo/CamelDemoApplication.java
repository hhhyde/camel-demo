package com.demo.cameldemo;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@ImportResource("classpath:my-camel.xml")
public class CamelDemoApplication {
    // TODO: 2018-06-20 ws组件
    // TODO: 2018-06-20 http 组件待实现 
    public static void main(String[] args) {
        SpringApplication.run(CamelDemoApplication.class, args);
    }



    @Bean
    public ServletRegistrationBean cxfServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new CXFServlet(), "/ws/*");
        registration.setName("CXFServlet");
        return registration;
    }
}
