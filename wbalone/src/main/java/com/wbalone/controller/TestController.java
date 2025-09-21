package com.wbalone.controller;

import com.wbalone.properties.CommonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName com.wbalone.controller
 *
 * @author lirui
 * @version JDK 17
 * @className TestController
 * @date 2025/9/21
 * @description TODO
 */
@RestController
public class TestController {

    @Autowired
    CommonProperties commonProperties;

    @GetMapping("/test")
    public String test() {
        return "hello world " + commonProperties.getTimeout();
    }
}
