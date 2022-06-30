package com.micropos.order.service;

import com.micropos.order.model.Cart;
import com.micropos.order.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Cart cart);

    List<Order> listOrders();

    Order deliverById(Integer orderId);
}
