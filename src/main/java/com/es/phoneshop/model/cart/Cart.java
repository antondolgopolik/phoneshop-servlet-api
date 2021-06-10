package com.es.phoneshop.model.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "Cart is empty";
        } else {
            StringBuilder builder = new StringBuilder("Cart: ");
            for (CartItem item : items) {
                builder.append(item).append("; ");
            }
            return builder.toString();
        }
    }
}
