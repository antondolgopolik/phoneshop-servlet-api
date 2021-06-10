package com.es.phoneshop.model.cart;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CartTest {

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
