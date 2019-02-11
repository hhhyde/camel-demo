package com.demo.cameldemo.quartz;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class QuartzDemo extends RouteBuilder {
    @Override
    public void configure() {

//        from("timer://MoveNewCustomersEveryHour?period=3600000")
//                .setBody(constant("select * from customer where create_time > (sysdate-1/24)"))
//                .to("jdbc:testdb")
//                .split(body())
////                .process(new MyCustomerProcessor()) //filter/transform results as needed
//                .setBody(simple("insert into processed_customer values('${body[ID]}','${body[NAME]}')"))
//                .to("jdbc:testdb");
    }

}
