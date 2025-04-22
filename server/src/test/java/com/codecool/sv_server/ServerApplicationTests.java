package com.codecool.sv_server;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import com.codecool.sv_server.service.HelloService;

@SpringBootTest
class ServerApplicationTests {
    @Autowired
    private HelloService helloService;

    static {
        io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    @Test
    void contextLoads() {
    }

    @Test
    void helloServiceReturnsCorrectMessage() {
        String message = helloService.sayHello();
        assertThat(message).isEqualTo("Hello, World!");
    }

}
