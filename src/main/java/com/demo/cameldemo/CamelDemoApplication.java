package com.demo.cameldemo;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

@SpringBootApplication
//@ImportResource("classpath:my-camel.xml")
public class CamelDemoApplication {
    // TODO: 2018-06-20 ws组件
    // TODO: 2018-06-20 http 组件待实现 
    public static void main(String[] args) {
        SpringApplication.run(CamelDemoApplication.class, args);
    }

    @Bean
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    public ServletRegistrationBean cxfServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new CXFServlet(), "/ws/*");
        registration.setName("CXFServlet");
        return registration;
    }



//    public void ss(){
//        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:2011/jmxrmi");
//        JMXConnector connector = JMXConnectorFactory.connect(url, null);
//        connector.connect();
//        MBeanServerConnection connection = connector.getMBeanServerConnection();
//
//        // 需要注意的是，这里的my-broker必须和上面配置的名称相同
//        ObjectName name = new ObjectName("my-broker:BrokerName=localhost,Type=Broker");
//        BrokerViewMBean mBean =  (BrokerViewMBean)MBeanServerInvocationHandler.newProxyInstance(connection,                     name, BrokerViewMBean.class, true);
//        // System.out.println(mBean.getBrokerName());
//
//        for(ObjectName queueName : mBean.getQueues()) {
//            QueueViewMBean queueMBean =                     (QueueViewMBean)MBeanServerInvocationHandler.newProxyInstance(connection, queueName, QueueViewMBean.class, true);
//            System.out.println("\n------------------------------\n");
//
//            // 消息队列名称
//            System.out.println("States for queue --- " + queueMBean.getName());
//
//            // 队列中剩余的消息数
//            System.out.println("Size --- " + queueMBean.getQueueSize());
//
//            // 消费者数
//            System.out.println("Number of consumers --- " + queueMBean.getConsumerCount());
//
//            // 出队数
//            System.out.println("Number of dequeue ---" + queueMBean.getDequeueCount() );
//        }
//
//    }
//    }
}
