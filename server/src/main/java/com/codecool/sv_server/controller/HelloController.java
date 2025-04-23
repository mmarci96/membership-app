package com.codecool.sv_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codecool.sv_server.service.HelloService;

@RestController
@RequestMapping("/api")
public class HelloController {
    private final HelloService helloService;

    @Autowired
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    ResponseEntity<String> hello() {
        var helloMessage = this.helloService.sayHello();
        return ResponseEntity.ok(helloMessage);
    }
}
