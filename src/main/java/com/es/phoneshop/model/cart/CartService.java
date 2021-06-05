package com.es.phoneshop.model.cart;

import com.es.phoneshop.exceptions.ProductNotEnoughException;

public interface CartService {

    Cart getCart();

    void add(Long productId, int quantity) throws ProductNotEnoughException;
}
