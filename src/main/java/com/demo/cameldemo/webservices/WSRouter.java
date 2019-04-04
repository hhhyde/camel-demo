package com.demo.cameldemo.webservices;

import com.demo.cameldemo.webservices.service_endpoint_interface.CommonInputBody;
import com.demo.cameldemo.webservices.service_endpoint_interface.CommonInputHead;
import com.demo.cameldemo.webservices.service_endpoint_interface.CommonInputRequest;
import com.demo.cameldemo.webservices.service_endpoint_interface.IncidentService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.DataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.RouteDefinition;
import org.springframework.stereotype.Component;

// TODO: 2019-04-05 待完成 
@Component
public class WSRouter extends RouteBuilder {
	@Override
	public void configure() throws Exception {

		from("cxf://aa?serviceClass=com.demo.cameldemo.webservices.service_endpoint_interface.IncidentService").log("$$$$$");

		startWSServer();

		CamelContext camelContext = getContext();
//
		CxfEndpoint cxfEndpoint = new CxfEndpoint();
		cxfEndpoint.setAddress("http://localhost:9555/dataCenter/webservices/incidentService");
		cxfEndpoint.setServiceClass(IncidentService.class);
		cxfEndpoint.setCamelContext(camelContext);
		cxfEndpoint.setDataFormat(DataFormat.POJO);
//
		try {
			camelContext.addEndpoint("myEndpoint", cxfEndpoint);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(111);


		from("timer://my-timer?repeatCount=1")
				.transform(constant("DummyBody"))
				.process(exchange -> {
					CommonInputRequest inputRequest = new CommonInputRequest();
					CommonInputHead head = new CommonInputHead();
					head.setKey("keyyyyyyyyyyy");
					head.setServiceID("5435435");
					head.setSystemID("545435");
					head.setURL("56654654");
					inputRequest.setHead(head);
					CommonInputBody body = new CommonInputBody();
					body.setMessage("body");
					inputRequest.setBody(body);
					exchange.getIn().setBody(inputRequest);
				})
				.to("cxf:http://localhost:9555/dataCenter/webservices/incidentService?serviceClass=com.toge.cameldemo.webservices.service_endpoint_interface.IncidentService")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						System.out.println(exchange.getExchangeId());
					}
				});

	}

	void startWSServer() throws Exception {
		RouteDefinition route = from("cxf:bean:incidentService")
				.process(exchange -> {
					System.out.println(exchange.getExchangeId());
				});
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("incidentService", IncidentService.class);
		CamelContext context = new DefaultCamelContext(registry);
		context.addRouteDefinition(route);
		context.start();
	}

}
