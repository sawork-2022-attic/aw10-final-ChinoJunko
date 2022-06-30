package com.micropos.delivery.mapper;


import com.micropos.dto.CartItemDto;
import com.micropos.delivery.model.Order;
import com.micropos.delivery.model.Item;
import com.micropos.dto.OrderDto;
import com.micropos.dto.ProductDto;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface DeliveryMapper {
    List<OrderDto> toOrderDtos(List<Order> orders);

    default Order toOrder(OrderDto orderDto) {
        Order order = new Order();
        order.id(orderDto.getId())
                .items(toItems(orderDto.getItems()))
                .status(orderDto.getStatus());
        return order;
    }

    default OrderDto toOrderDto(Order order) {
        return new OrderDto().id(order.id())
                .items(toCartItemDtos(order.items()))
                .status(order.status());
    }

    default List<CartItemDto> toCartItemDtos(List<Item> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        List<CartItemDto> list = new ArrayList<>(items.size());
        for (Item item : items) {
            list.add(toCartItemDto(item));
        }

        return list;
    }

    default List<Item> toItems(List<CartItemDto> itemDtos) {
        if (itemDtos == null || itemDtos.isEmpty()) {
            return null;
        }
        List<Item> list = new ArrayList<>(itemDtos.size());
        for (CartItemDto itemDto : itemDtos) {
            list.add(toItem(itemDto));
        }

        return list;
    }

    default CartItemDto toCartItemDto(Item item) {
        return new CartItemDto().id(item.id())
                .amount(item.quantity())
                .product(getProductDto(item));
    }

    default Item toItem(CartItemDto itemDto) {
        return new Item().id(itemDto.getId())
                .productId(itemDto.getProduct().getId())
                .productName(itemDto.getProduct().getName())
                .quantity(itemDto.getAmount())
                .unitPrice(itemDto.getProduct().getPrice());
    }

    default ProductDto getProductDto(Item item) {
        return new ProductDto().id(item.productId())
                .name(item.productName())
                .price(item.unitPrice());
    }

}
