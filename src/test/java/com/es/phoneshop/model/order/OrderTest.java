package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartTest;

public class OrderTest {

    public static Order[] getMockOrders(int n) {
        Order[] orders = new Order[n];
        Cart[] carts = CartTest.getMockCarts(n);
        DefaultOrderService defaultOrderService = DefaultOrderService.getInstance();
        for (int i = 0; i < n; i++) {
            orders[i] = defaultOrderService.getOrder(carts[i]);
        }
        return orders;
    }
}
