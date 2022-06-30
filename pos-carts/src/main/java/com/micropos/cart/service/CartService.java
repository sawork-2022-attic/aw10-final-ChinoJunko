package com.micropos.cart.service;

import com.micropos.cart.model.Cart;
import com.micropos.cart.model.Item;

import java.util.List;
import java.util.Optional;

public interface CartService {

    Cart create();

    Double checkout(Integer cartId);

    Cart add(Integer cartId, Item item);

    Cart get(Integer cartId);

    Integer test();
}
