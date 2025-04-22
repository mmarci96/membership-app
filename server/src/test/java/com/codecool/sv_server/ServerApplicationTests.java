package com.codecool.sv_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codecool.sv_server.service.HelloService;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ServerApplicationTests {
    @Autowired
    private HelloService helloService;

    // @Test
    // void contextLoads() {
    // }

    @Test
    void helloServiceReturnsCorrectMessage() {
        String message = helloService.sayHello();
        assertThat(message).isEqualTo("Hello, World!");
    }

}
