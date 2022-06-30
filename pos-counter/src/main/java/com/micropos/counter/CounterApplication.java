package com.micropos.counter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CounterApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CounterApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }
}
