package com.demo.cameldemo.webservices.cxf;

import com.demo.cameldemo.webservices.cxf.service.impl.BaseicQueryImpl;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class CxfRoute extends RouteBuilder {

    public static final String BASEICQUERY_SERVICE_URL = "http://localhost:9999/abc";
    public static final String BASEICQUERY_SERVICE_URL1 = "/baseQueryService";
    public static final String BASEQUERY_SERVICE_URL_ADDRESS = "cxf://" + BASEICQUERY_SERVICE_URL + "?serviceClass=com.demo.cameldemo.webservices.cxf.service.BaseicQuery";

    @Override
    public void configure() throws Exception {
        from(BASEQUERY_SERVICE_URL_ADDRESS).process(new Cxfprocessor(new BaseicQueryImpl())).log("ddd");
        from("cxf://http://0.0.0.0:9998/abcd?serviceClass=com.demo.cameldemo.webservices.cxf.service.BaseicQuery").process(new Cxfprocessor(new BaseicQueryImpl())).log("ddd");
        from("cxf://http://localhost:9998/abcde?serviceClass=com.demo.cameldemo.webservices.cxf.service.BaseicQuery").process(new Cxfprocessor(new BaseicQueryImpl())).log("ddd");
    }
}
