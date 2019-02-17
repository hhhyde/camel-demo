package com.demo.cameldemo.mq.kafka;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class kafkaDemo extends RouteBuilder {
	@Override
	public void configure() throws Exception {

		// setup kafka component with the brokers
		KafkaComponent kafka = new KafkaComponent();
		kafka.setBrokers("119.27.161.183:9092");
		getContext().addComponent("kafka", kafka);

		from("timer://abbb?repeatCount=2")
				.setBody(constant("Message from Camel"))          // Message to send
				.setHeader(KafkaConstants.KEY, constant("Camel")) // Key of the message
				.log("发送成功")
				.to("kafka:119.27.161.183:9092?topic=test");

//        from("kafka:test?topic=test&brokers=119.27.161.183:9092&groupId=test")
//                .log("Message received from Kafka : ${body}")
//                .log("    on the topic ${headers[kafka.TOPIC]}")
//                .log("    on the partition ${headers[kafka.PARTITION]}")
//                .log("    with the offset ${headers[kafka.OFFSET]}")
//                .log("    with the key ${headers[kafka.KEY]}");

//        from("direct:start").log("777");

//        from("direct:start").process(exchange -> {
//            exchange.getIn().setBody("Test Message from Camel Kafka Component Final",String.class);
//            exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, 0);
//            exchange.getIn().setHeader(KafkaConstants.KEY, "1");
//        }).to("kafka:119.27.161.183:9092?topic=test");

		// TODO: 2019-02-14 kafka文件未实现
		from("direct:file").process(exchange -> {
//            exchange.getIn().setBody("Test Message from Camel Kafka Component Final", String.class);
			exchange.getIn().setBody(new File("D:/logo.png"), Byte.class);
			exchange.getIn().setHeader(KafkaConstants.PARTITION_KEY, 0);
			exchange.getIn().setHeader(KafkaConstants.KEY, "1");
		}).log("发送文件")
				.to("kafka:119.27.161.183:9092?topic=test");


		from("kafka:119.27.161.183:9092?topic=test&groupId=testing&autoOffsetReset=earliest&consumersCount=1")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange)
							throws Exception {
						String messageKey = "";
						if (exchange.getIn() != null) {
							Message message = exchange.getIn();
							Integer partitionId = (Integer) message
									.getHeader(KafkaConstants.PARTITION);
							String topicName = (String) message
									.getHeader(KafkaConstants.TOPIC);
							if (message.getHeader(KafkaConstants.KEY) != null)
								messageKey = (String) message
										.getHeader(KafkaConstants.KEY);
							Object data = message.getBody();


							System.out.println("topicName :: "
									+ topicName + " partitionId :: "
									+ partitionId + " messageKey :: "
									+ messageKey + " message :: "
									+ data + "\n");
						}
					}
				});
	}
}
