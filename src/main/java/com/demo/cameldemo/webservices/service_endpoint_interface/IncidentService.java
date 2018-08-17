package com.demo.cameldemo.webservices.service_endpoint_interface;


import org.springframework.stereotype.Component;

/**
 * @Title:
 * @Description:对外暴露webServices接口
 * @Author:Rose
 * @Since:2018年1月10日
 * @Version:1.1.0
 */

@Component("incidentService")
public interface IncidentService {
    /**
     * @param input
     * @return
     * @Description:
     */
    CommonOutputResponse commonWebServices(CommonInputRequest requestText);

}
