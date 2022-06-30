package com.micropos.delivery;

import com.micropos.delivery.mapper.DeliveryMapper;
import com.micropos.delivery.service.DeliveryService;
import com.micropos.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import java.util.function.Consumer;

@EnableEurekaClient
@SpringBootApplication
public class DeliveryApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DeliveryApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Bean
    public Consumer<OrderDto> order() {
        return orderDto -> deliveryService.createDelivery(deliveryMapper.toOrder(orderDto));
    }
}
