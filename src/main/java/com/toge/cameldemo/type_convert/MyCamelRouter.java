package com.toge.cameldemo.type_convert;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MyCamelRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
//        ftpRoute();
    }

    private void ftpRoute() {
        // 后面追加一个bean节点获取转发之后的exchange?
        getContext().getTypeConverterRegistry().addTypeConverter(String.class, File.class, new File2StrConvert());
        from("ftp://192.168.0.212:5555/?username=ff&password=ff&localWorkDirectory=/dmp123&delay=10000")
                .to("file://D://ftp")
                .bean(new ExchangeHandler(), "say");
    }
}
