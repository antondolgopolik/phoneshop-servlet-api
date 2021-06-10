package com.es.phoneshop.model.recentlyviewed;

import com.es.phoneshop.model.product.Product;

import java.util.LinkedList;
import java.util.Queue;

public class RecentlyViewed {
    private final Queue<Product> products = new LinkedList<>();

    public Queue<Product> getProducts() {
        return products;
    }
}
