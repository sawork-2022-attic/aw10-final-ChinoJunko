package com.micropos.cart.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropos.cart.mapper.CartMapper;
import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;
import com.micropos.cart.repository.CartRepository;
import com.micropos.cart.repository.ItemRepository;
import com.micropos.dto.CartDto;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {


    private final String COUNTER_URL = "http://POS-COUNTER/counter-api";

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    WebClient.Builder builder;

    private CartMapper cartMapper;

    @Autowired
    public void setCartMapper(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Override
    public Cart create() {
        return cartRepository.save(new Cart().items(new ArrayList<>()));
    }

    @Override
    public Double checkout(Integer cartId) {
        if(!cartRepository.existsById(cartId))  return -1.0;
        CartDto cartDto = cartMapper.toCartDto(cartRepository.findById(cartId).get());
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = null;
        try {
            request = new HttpEntity<>(mapper.writeValueAsString(Mono.just(cartDto)), headers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Double total = builder.build().post().uri(COUNTER_URL+ "/counter/checkout").contentType(MediaType.APPLICATION_JSON).body(Mono.just(cartDto),cartDto.getClass()).retrieve().bodyToMono(Double.class).block();
        return total;
    }


    @Override
    public Cart add(Integer cartId, Item item) {
        if (cartRepository.existsById(cartId)) {
            Cart cart = cartRepository.findById(cartId).get();
            itemRepository.save(item);
            cart.items().add(item);
            cartRepository.save(cart);
            return cart;
        }
        return null;
    }

    @Override
    public Cart get(Integer cartId) {
        return cartRepository.existsById(cartId)?cartRepository.findById(cartId).get():null;
    }

    @Override
    public Integer test() {
        return null;
    }
}
