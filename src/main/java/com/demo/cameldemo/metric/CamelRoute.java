package com.demo.cameldemo.metric;

import com.alibaba.fastjson.JSON;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Endpoint(id = "camel-route")
public class CamelRoute {

    @Autowired
    CamelContext camelContext;

    @ReadOperation
    public List invoke() {
        return camelContext.getRouteDefinitions().stream().map(route -> route.getId()).collect(Collectors.toList());
    }
}
