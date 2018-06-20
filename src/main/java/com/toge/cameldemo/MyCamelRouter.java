package com.toge.cameldemo;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.TypeConverterSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;

@Component
public class MyCamelRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        ftpRoute();
    }

    private void ftpRoute() {
        getContext().getTypeConverterRegistry().addTypeConverter(File.class,String.class,new type());
        // 后面追加一个bean节点获取转发之后的exchange?
        // TODO: 2018-06-20 是否是转发之后的exchange?有没有转发完成状态位
//        from("ftp://192.168.0.212:5555/?username=ff&password=ff&localWorkDirectory=/dmp123&delay=10000")
        from("timer://aaa?repeatCount=1")
                .bean(new MyBean(), "say")
                .to("file://D://ftp");
    }

    // TODO: 2018-06-20 为什么不直接用http组件
    private void servletRotue(){
        from("ftp://192.168.0.212:5555/?username=ff&password=ff&localWorkDirectory=/dmp123&delay=10000").to("file://D://ftp");
    }

    private void httpRoute(){
    }
}

class type extends TypeConverterSupport{

    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        System.out.println(type);
        return null;
    }
}
