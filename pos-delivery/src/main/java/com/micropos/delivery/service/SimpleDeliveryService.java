package com.micropos.delivery.service;

import com.micropos.delivery.model.Item;
import com.micropos.delivery.model.Order;
import com.micropos.delivery.repository.ItemRepository;
import com.micropos.delivery.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SimpleDeliveryService implements DeliveryService {

    private final Random random = new Random();

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void createDelivery(Order order) {
        if (order==null) {
            log.error("Failed to save delivery: " + order);
            return;
        }
        Order newOrder = orderRepository.save(new Order().id(order.id()).status(order.status()));
        for (Item item : order.items()) {
            itemRepository.save(item);
        }
        newOrder.items(order.items());
        orderRepository.save(newOrder);
        log.info("Create new Delivery: " + order);
    }

    @Override
    public Order findDelivery(Integer deliveryId) {
        Optional<Order> order = orderRepository.findById(deliveryId);
        if(order.isEmpty()) return null;
        return order.get();
    }

}
