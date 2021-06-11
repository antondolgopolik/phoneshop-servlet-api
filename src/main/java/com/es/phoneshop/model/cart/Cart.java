package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Cart implements Serializable {
    private final List<CartItem> items = new ArrayList<>();
    private final Currency currency = Currency.getInstance("USD");
    private BigDecimal totalCost = new BigDecimal(0);
    private int totalQuantity;

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
