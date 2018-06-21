package com.toge.cameldemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("echo")
    public String echo(@RequestParam(value = "echo", required = false) String echo, @RequestParam(value = "echo123", required = false) String echo123, ServletRequest request) {
        return echo;
    }
}
