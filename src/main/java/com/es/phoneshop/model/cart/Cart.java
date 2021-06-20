package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Cart implements Serializable, Cloneable {
    private final List<CartItem> items;
    private final Currency currency;
    private BigDecimal totalCost;
    private int totalQuantity;

    public Cart(Cart cart) {
        items = new ArrayList<>(cart.items.size());
        for (CartItem item : cart.items) {
            try {
                items.add((CartItem) item.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        currency = cart.currency;
        totalCost = cart.totalCost;
        totalQuantity = cart.totalQuantity;
    }

    public Cart() {
        items = new ArrayList<>();
        currency = Currency.getInstance("USD");
        totalCost = new BigDecimal(0);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
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
