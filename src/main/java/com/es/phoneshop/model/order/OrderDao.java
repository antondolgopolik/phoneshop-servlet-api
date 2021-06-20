package com.es.phoneshop.model.order;

public interface OrderDao {

    Order get(String id);

    void save(Order order);
}
