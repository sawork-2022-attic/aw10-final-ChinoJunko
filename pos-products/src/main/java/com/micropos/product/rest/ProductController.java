package com.micropos.product.rest;

import com.micropos.api.ProductsApi;
import com.micropos.dto.ProductDto;
import com.micropos.product.mapper.ProductMapper;
import com.micropos.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("products-api")
@RestController
public class ProductController implements ProductsApi {

    private final ProductMapper productMapper;

    private final ProductService productService;


    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @Override
    public Mono<ResponseEntity<ProductDto>> showProductById(String productId, ServerWebExchange exchange) {
        return Mono.justOrEmpty(
                        productMapper.toProductDto(productService.getProduct(productId))
                )
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Flux<ProductDto>>> listProducts(ServerWebExchange exchange) {
        return Mono.just(Flux.fromArray(new ArrayList<>(productMapper.toProductsDto(this.productService.products()))
                        .toArray(ProductDto[]::new)))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
