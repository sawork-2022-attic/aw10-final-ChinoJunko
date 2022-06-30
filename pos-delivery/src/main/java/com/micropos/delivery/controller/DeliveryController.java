package com.micropos.delivery.controller;

import com.micropos.api.DeliveryApi;
import com.micropos.delivery.mapper.DeliveryMapper;
import com.micropos.delivery.model.Order;
import com.micropos.delivery.service.DeliveryService;
import com.micropos.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/delivery-api")
public class DeliveryController implements DeliveryApi {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Override
    public Mono<ResponseEntity<OrderDto>> getDeliveryById(Integer deliveryId, ServerWebExchange exchange) {
        return Mono.justOrEmpty(
                deliveryMapper.toOrderDto(deliveryService.findDelivery(deliveryId))
                )
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
