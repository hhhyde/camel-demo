package com.demo.cameldemo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping(value = "echo", method = {RequestMethod.GET, RequestMethod.POST})
    public String echo(@RequestBody String body) {
        return body;
    }

}
