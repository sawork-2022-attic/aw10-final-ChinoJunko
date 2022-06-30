package com.micropos.cart.rest;

import com.micropos.api.CartsApi;
import com.micropos.cart.mapper.CartMapper;
import com.micropos.cart.model.Cart;
import com.micropos.cart.service.CartService;
import com.micropos.dto.CartDto;
import com.micropos.dto.CartItemDto;
import com.micropos.dto.ProductDto;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.http.WebClientEurekaHttpClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("carts-api")
public class CartController implements CartsApi {

    private final String PRODUCTS_URL = "http://POS-PRODUCTS/products-api";

    @Autowired
    private CartService cartService;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    WebClient.Builder builder;

    @Override
    public Mono<ResponseEntity<CartDto>> addItemToCart(Integer cartId, Integer productId, Integer productCount, ServerWebExchange exchange) {
        WebClient webClient = builder.build();
        CartItemDto cartItemDto = new CartItemDto().id(1)
                .product(webClient.get().uri(PRODUCTS_URL+ "/products/"+productId).retrieve().bodyToMono(ProductDto.class).block())
                .amount(productCount);
        cartService.add(cartId, cartMapper.toItem(cartItemDto, cartId));
        return showCartById(cartId, exchange);
    }

    @Override
    public Mono<ResponseEntity<Integer>> createCart(ServerWebExchange exchange) {
        return Mono.just(cartService.create().id())
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CartDto>> showCartById(Integer cartId, ServerWebExchange exchange) {
        return Mono.justOrEmpty(
                        cartMapper.toCartDto(cartService.get(cartId))
                )
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Double>> showCartTotal(Integer cartId, ServerWebExchange exchange) {

        Double total = cartService.checkout(cartId);

        if (total == -1d) {
            return Mono.just(ResponseEntity.notFound().build());
        }
        return Mono.just(total).map(ResponseEntity::ok);
    }
}
