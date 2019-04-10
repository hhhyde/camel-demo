package com.demo.cameldemo.ftp;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FtpProcess extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // 同步完成之后的文件转移到.done这个文件夹,不然的话不停的重复下载
        from("sftp://ubuntu@119.27.161.183/Music?password=333333aa&localWorkDirectory=/Downloads&move=.done")
                .process(exchange -> {
                    System.out.println(exchange);
                })
                .to("file:abc4444444")
        ;
    }
}
