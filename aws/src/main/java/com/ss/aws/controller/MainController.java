package com.ss.aws.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MainController {

    @GetMapping("/aws/v1")
    public String awsV1(@RequestParam(defaultValue = "1") Integer num) {

        if(num == 1) {
            log.info("aws/v1 호출됨! info 로그#################");
        } else if (num == -1) {
            log.error("aws/v1 호출됨! error 로그#################");
        } else if(num == 0 ) {
            log.warn("aws/v1 호출됨! warn 로그#################");
        }

        return "<h1> aws v1</h1>";
    }
}
