package com.micropos.delivery.repository;

import com.micropos.delivery.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
}
