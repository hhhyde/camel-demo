package com.demo.cameldemo.http;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class HttpRouter extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		http();
	}


	private void simpleRoute() {
		from("timer://aaa?repeatCount=1")
				.process(new BodyProcess())
				.to("http://localhost:8080/test/echo")
				.bean(new ResponseHandler(), "handler");
	}

	/**
	 * 监听本机的12456端口,重定向到8080端口
	 */
	private void httpRoute() {
		from("jetty:http://0.0.0.0:12456")
				.process(new BodyProcess())
				.to("http://localhost:8080/test/echo?bridgeEndpoint=true")
				.bean(new ResponseHandler(), "handler");
	}

	private void http() {
		from("servlet://ss").process(exchange -> {
			System.out.println(exchange.getIn().getBody(String.class));
		}).to("http://223.87.179.49:9998/kfoa/runtime/service/depts?bridgeEndpoint=true");
	}
}
