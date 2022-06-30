package com.micropos.delivery.service;

import com.micropos.delivery.model.Order;

import java.util.List;

public interface DeliveryService {

    void createDelivery(Order order);

    Order findDelivery(Integer deliveryId);
}
