package com.micropos.delivery.repository;

import com.micropos.delivery.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Integer> {
}
