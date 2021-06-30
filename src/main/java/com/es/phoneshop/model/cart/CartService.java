package com.es.phoneshop.model.cart;

import com.es.phoneshop.exceptions.ProductNotEnoughException;

import javax.servlet.http.HttpServletRequest;

public interface CartService {

    Cart getCart(HttpServletRequest httpServletRequest);

    Cart copyCart(Cart cart);

    void add(Cart cart, Long productId, int quantity) throws ProductNotEnoughException;

    void delete(Cart cart, Long productId);

    void clear(Cart cart);

    void update(Cart cart, Long productId, int quantity) throws ProductNotEnoughException;
}
