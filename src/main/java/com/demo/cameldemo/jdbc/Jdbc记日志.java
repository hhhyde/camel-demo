package com.demo.cameldemo.jdbc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.SimpleRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.UUID;

//@Component
public class Jdbc记日志 extends RouteBuilder {

    @Autowired
    DefaultListableBeanFactory defaultListableBeanFactory;

    @Override
    public void configure() throws Exception {

        BeanDefinitionBuilder builder = null;
            builder = BeanDefinitionBuilder.genericBeanDefinition(
                    org.apache.commons.dbcp.BasicDataSource.class);
            builder.addPropertyValue("initialSize", 100);
            builder.addPropertyValue("maxActive", 20000);
            builder.addPropertyValue("maxIdle", 1000);
            builder.addPropertyValue("minIdle", 200);
            builder.addPropertyValue("maxWait", 999999);


        builder.addPropertyValue("driverClassName", "oracle.jdbc.driver.OracleDriver");
        builder.addPropertyValue("url", "jdbc:oracle:thin:@119.27.161.183:49161:XE");
        builder.addPropertyValue("username", "DATAPLATFORM");
        builder.addPropertyValue("password", "DATAPLATFORM");

        defaultListableBeanFactory.registerBeanDefinition("db"+"", builder.getBeanDefinition());



        from("quartz2://Jdbc记日志?cron=0/5+0/1+12-18+?+*+MON-FRI")
                .setBody(constant("select * from SYS_USER"))
                .to("jdbc:db")
                .process(exchange->{
                    exchange.setProperty("ServiceLogID", UUID.randomUUID().toString());
                    System.out.println("0|||"+exchange.getProperty("CamelCorrelationId"));
                        }
                )
                .split()
                .simple("${body}")
                .process(exchange->{

                            System.out.println("1|||"+exchange.getExchangeId());
                        }
                )
                .process(new Processor() {
                    public void process(Exchange xchg) throws Exception {
                        System.out.println("2|||"+xchg.getExchangeId());
                        Map<String, Object> row = xchg.getIn().getBody(Map.class);
//                        System.out.println("Processing constant....." + row);
//                        if (row != null && row.size() > 0) {
//                            System.out.println(row.get("id") + ":" + row.get("amount") + ":"
//                                    + row.get("processed") + ":" + row.get("book_id"));
//                        }
                    }
                })
        .process(exchange -> {
            System.out.println("3|||"+exchange.getExchangeId());
        })
        ;

    }
}
