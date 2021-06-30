package com.es.phoneshop.model.order;

import com.es.phoneshop.exceptions.NoOrderWithSuchIdException;

import java.util.HashMap;
import java.util.Map;

public class HashMapOrderDao implements OrderDao {
    private final Map<String, Order> orders = new HashMap<>();

    public static HashMapOrderDao getInstance() {
        return SingletonInstanceHolder.instance;
    }

    private HashMapOrderDao() {
    }

    @Override
    public Order get(String id) throws NoOrderWithSuchIdException {
        // Find order
        Order order;
        synchronized (orders) {
            order = orders.get(id);
        }
        // Handle result
        if (order != null) {
            return order;
        } else {
            throw new NoOrderWithSuchIdException(id);
        }
    }

    @Override
    public void save(Order order) {
        synchronized (orders) {
            orders.put(order.getId(), order);
        }
    }

    private static class SingletonInstanceHolder {
        private static final HashMapOrderDao instance = new HashMapOrderDao();
    }
}
