package com.micropos.product;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class ProductsApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ProductsApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }
}