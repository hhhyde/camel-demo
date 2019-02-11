package com.demo.cameldemo.webservices.cxf;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class Cxfprocessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Cxfprocessor.class);

    private Class<?> beanClass;
    private Object instance;

    public Cxfprocessor(Object instance) {
        this.instance = instance;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String inputMessage = exchange.getIn().getBody(String.class);
        LOGGER.info("inputMessage : " + inputMessage);

        String operationName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        Method method = findMethod(operationName, exchange.getIn().getBody(Object[].class));

        Object response = method.invoke(instance, exchange.getIn().getBody(Object[].class));

        exchange.getOut().setBody(response);
    }

    private Method findMethod(String operationName, Object[] parameters) throws SecurityException, NoSuchMethodException {
        return beanClass.getMethod(operationName, getParameterTypes(parameters));
    }

    private Class<?>[] getParameterTypes(Object[] parameters) {

        if (parameters == null) {
            return new Class[0];
        }
        Class<?>[] answer = new Class[parameters.length];
        int i = 0;
        for (Object object : parameters) {
            answer[i] = object.getClass();
            i++;
        }
        return answer;
    }

}
