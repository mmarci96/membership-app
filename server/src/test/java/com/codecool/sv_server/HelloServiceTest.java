package com.codecool.sv_server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.codecool.sv_server.service.HelloService;

@SpringBootTest
@ActiveProfiles("test")
public class HelloServiceTest {
    @Autowired
    private HelloService helloService;

    @Test
    void testHelloService() {
        var msg = helloService.sayHello();
        assertThat(msg).isEqualTo("Hello, World!");
    }
}
