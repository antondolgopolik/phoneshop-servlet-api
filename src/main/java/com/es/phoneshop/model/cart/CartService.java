package com.es.phoneshop.model.cart;

import com.es.phoneshop.exceptions.ProductNotEnoughException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface CartService {

    Cart getCart(HttpServletRequest httpServletRequest);

    void add(Cart cart, Long productId, int quantity) throws ProductNotEnoughException;
}
