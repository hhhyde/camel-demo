package com.demo.cameldemo.mq.activemq;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActiveMqDemo extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setBrokerURL("tcp://119.27.161.183:61616");

        getContext().addComponent("activemq", activeMQComponent);

        重复消费();

        List gg = getContext().getEndpoints().stream().map(endpoint -> endpoint.getEndpointUri()).collect(Collectors.toList());
        System.out.println(gg);

    }


    /**
     * 消息共享
     */
    public void messageShare() {
        // 同一个queue的消费者,共享这个queue的消息
        from("activemq:queue:foo")
                .to("log:sample");
        from("activemq:queue:foo")
                .to("log:sample1");

        from("timer:bar?repeatCount=10&period=500")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
                })
                .to("activemq:queue:foo");
    }

    /**
     * 消息分发
     */
    public void messageDispatch() {
        // 同一个topic的消费者,共享这个topic的消息
        from("activemq:topic:foo")
                .to("log:sample");
        from("activemq:topic:foo")
                .to("log:sample1");

        from("timer:bar?repeatCount=10&period=500")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
                })
                .to("activemq:topic:foo");
    }

    /**
     * 死信队列
     * 手动重试,可以通过api将死信队列中的数据移动到指定队列(如原队列中重新消费)
     */
    void deadLetterQueue() {
        // 同一个topic的消费者,共享这个topic的消息

        errorHandler(
                // 重试之后进死信通道
                deadLetterChannel("activemq:queue:dead")
                        // 使用原始消息,即使在路由中被修改了还是会还原
                        .useOriginalMessage()
                        // 最大重试次数n,进行正常执行后追加的重试.最终执行次数1+n
                        .maximumRedeliveries(0)
                        // 重试延迟ms
                        .redeliveryDelay(1000)
                        .onRedelivery(exchange -> {
                            // 每次重试进来
                            System.out.println("重试");
                        })
                        .onPrepareFailure(exchange -> {
                            // 只有放进死信,才进来一次
                            exchange.getIn().setHeader("OriginRouteId", exchange.getFromRouteId());
                        })
        );

        from("timer:bar?repeatCount=10&period=1000")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
                })
                .to("activemq:topic:foo");

        from("activemq:topic:foo")
                .routeId("死信队列测试")
                .process(exchange -> {
                    // 重试期间才会产生
                    int redeliveryCounter = exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER, 0, Integer.class);
                    int redeliveryMaxCounter = exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER, 0, Integer.class);

                    long time = System.currentTimeMillis();
                    System.out.println(time + "|" + exchange.getIn().getBody() + "|已重试次数" + redeliveryCounter + "|最大重试次数" + redeliveryMaxCounter);
                    if (time % 10 % 2 == 1) {
                        // 最后一位是奇数
                        throw new RuntimeException("抛出异常.");
                    }
                    System.out.println("成功消费" + exchange.getIn().getBody());
                })
                .to("log:sample");
    }

    /**
     * 消费同一个topic的两个消费者,如果报错,那么
     * 1. 会不会记录两条死信
     */
    void deadLetter2(){
        errorHandler(
                // 重试之后进死信通道
                deadLetterChannel("activemq:queue:dead")
                        // 使用原始消息,即使在路由中被修改了还是会还原
                        .useOriginalMessage()
                        // 最大重试次数n,进行正常执行后追加的重试.最终执行次数1+n
                        .maximumRedeliveries(0)
                        // 重试延迟ms
                        .redeliveryDelay(1000)
                        .onRedelivery(exchange -> {
                            // 每次重试进来
                            System.out.println("重试");
                        })
                        .onPrepareFailure(exchange -> {
                            // 只有放进死信,才进来一次
                            System.out.println(exchange.getIn().getBody() + " 放进死信");
                            exchange.getIn().setHeader("OriginRouteId", exchange.getFromRouteId());
                        })
        );

        from("timer:bar?repeatCount=10&period=1000")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
                })
                .to("activemq:queue:foo1");

        from("activemq:queue:foo1")
                .routeId("死信队列测试1")
                .log("1")
                .process(exchange -> {
                    // 重试期间才会产生
                    int redeliveryCounter = exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER, 0, Integer.class);
                    int redeliveryMaxCounter = exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER, 0, Integer.class);

                    long time = System.currentTimeMillis();
                    System.out.println(time + "|" + exchange.getIn().getBody() + "|已重试次数" + redeliveryCounter + "|最大重试次数" + redeliveryMaxCounter);
                    if (time % 10 % 2 == 1) {
                        // 最后一位是奇数
//                        throw new RuntimeException("抛出异常.");
                    }
                    System.out.println("成功消费" + exchange.getIn().getBody());
                })
                .to("log:sample");

        from("activemq:queue:foo1")
                .routeId("死信队列测试2")
                .log("2")
                .process(exchange -> {
                    // 重试期间才会产生
                    int redeliveryCounter = exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER, 0, Integer.class);
                    int redeliveryMaxCounter = exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER, 0, Integer.class);

                    long time = System.currentTimeMillis();
                    System.out.println(time + "|" + exchange.getIn().getBody() + "|已重试次数" + redeliveryCounter + "|最大重试次数" + redeliveryMaxCounter);
                    if (time % 10 % 2 == 1) {
                        // 最后一位是奇数
//                        throw new RuntimeException("抛出异常.");
                    }
                    System.out.println("成功消费" + exchange.getIn().getBody());
                })
                .to("log:sample");
    }


    /**
     * 从一个来源分发给多个队列,每个队列单独有死信异常处理
     *
     */

    void 重复消费(){
        errorHandler(
                // 重试之后进死信通道
                deadLetterChannel("activemq:queue:dead")
                        // 使用原始消息,即使在路由中被修改了还是会还原
                        .useOriginalMessage()
                        // 最大重试次数n,进行正常执行后追加的重试.最终执行次数1+n
                        .maximumRedeliveries(0)
                        // 重试延迟ms
                        .redeliveryDelay(1000)
                        .onRedelivery(exchange -> {
                            // 每次重试进来
                            System.out.println("重试");
                        })
                        .onPrepareFailure(exchange -> {
                            // 只有放进死信,才进来一次
                            System.out.println(exchange.getIn().getBody() + " 放进死信");
                            exchange.getIn().setHeader("OriginRouteId", exchange.getFromRouteId());
                        })
        );



//        OA进来的服务,用于消息分发
        from("timer:bar?repeatCount=10&period=500")
                .process(exchange -> {
                    exchange.getIn().setBody("测试消息" + DateUtil.formatDate(new Date()), String.class);
                })
                .to("activemq:queue:重复消费-消息分发1")

                .to("activemq:queue:重复消费-消息分发2");

//        异构系统1服务
        from("activemq:queue:重复消费-消息分发1")
                .log("消费者1消费")
                .to("log:转发给异构系统1");

//        异构系统2服务
        from("activemq:queue:重复消费-消息分发2")
                .log("消费者2消费")
                .to("log:转发给异构系统2");

        from("activemq:queue:dead")
                .process(ex->{
                    System.out.println(String.format("从死信中拉取消息:%s", ex.getIn().getHeader("OriginRouteId")));
                });
    }
}
