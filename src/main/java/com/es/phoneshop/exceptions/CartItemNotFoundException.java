package com.es.phoneshop.exceptions;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(long id) {
        super("Cart item with product ID = " + id + " doesn't exist!");
    }
}
