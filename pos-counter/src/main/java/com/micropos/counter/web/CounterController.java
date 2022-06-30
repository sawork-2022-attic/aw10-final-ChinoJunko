package com.micropos.counter.web;

import com.micropos.api.CounterApi;
import com.micropos.counter.service.CounterService;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("counter-api")
public class CounterController implements CounterApi {

    @Autowired
    private CounterService counterService;

    @Autowired
    public void setCounterService(CounterService counterService) {
        this.counterService = counterService;
    }

    @Override
    public Mono<ResponseEntity<Double>> checkout(Mono<CartDto> cartDto, ServerWebExchange exchange) {
        CartDto cart = cartDto.block();
        System.out.println("new Carts");
        return Mono.justOrEmpty(this.counterService.getTotal(cart))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
