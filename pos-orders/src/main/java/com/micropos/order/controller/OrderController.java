package com.micropos.order.controller;

import com.micropos.api.OrdersApi;
import com.micropos.dto.CartDto;
import com.micropos.dto.OrderDto;
import com.micropos.order.mapper.OrderMapper;
import com.micropos.order.model.Cart;
import com.micropos.order.model.Order;
import com.micropos.order.service.OrderService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("orders-api")
@RestController
public class OrderController implements OrdersApi {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StreamBridge streamBridge;

    @Override
    public Mono<ResponseEntity<OrderDto>> createOrder(Mono<CartDto> cartDto, ServerWebExchange exchange) {
        Order order = orderService.createOrder(orderMapper.toCart(cartDto.block()));
        if (order != null) {
            OrderDto orderDto = orderMapper.toOrderDto(order);
            streamBridge.send("order-out-0", orderDto);
            return Mono.just(orderDto).map(ResponseEntity::ok);
        }
        return Mono.just(ResponseEntity.badRequest().build());
    }

    @Override
    public Mono<ResponseEntity<Flux<OrderDto>>> listOrders(ServerWebExchange exchange) {
        return Mono.justOrEmpty(
                        Flux.fromArray(orderMapper.toOrderDtos(orderService.listOrders()).toArray(OrderDto[]::new))
                )
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<OrderDto>> deliverOrder(Integer orderId, ServerWebExchange exchange) {
        return Mono.justOrEmpty(
                        orderMapper.toOrderDto(orderService.deliverById(orderId))
                )
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
