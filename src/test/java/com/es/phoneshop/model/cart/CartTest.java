package com.es.phoneshop.model.cart;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CartTest {

    public static Cart[] getMockCarts(int n) {
        Cart[] carts = new Cart[n];
        DefaultCartService defaultCartService = DefaultCartService.getInstance();
        for (int i = 0; i < n; i++) {
            carts[i] = new Cart();
            defaultCartService.add(carts[i], (long) i, 10);
        }
        return carts;
    }

    public static Cart getMockCart() {
        Cart cart = new Cart();
        DefaultCartService defaultCartService = DefaultCartService.getInstance();
        defaultCartService.add(cart, 0L, 10);
        return cart;
    }

    @Test
    public void testEmptyCartToString() {
        Cart cart = new Cart();
        Assert.assertFalse(cart.toString().isEmpty());
    }

    @Test
    public void testCartToString() {
        Cart cart = new Cart();
        cart.getItems().addAll(Arrays.asList(CartItemTest.getMockCartItems(10)));
        Assert.assertFalse(cart.toString().isEmpty());
    }
}
