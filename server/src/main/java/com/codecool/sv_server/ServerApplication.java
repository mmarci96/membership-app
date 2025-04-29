package com.codecool.sv_server;

import com.codecool.sv_server.config.RsaKeyProperties;
import com.codecool.sv_server.utils.DotenvLoader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ServerApplication {

    public static void main(String[] args) {
        DotenvLoader.loadEnv();
        SpringApplication.run(ServerApplication.class, args);
    }
}
