package com.es.phoneshop.model.product;

import com.es.phoneshop.exceptions.NoProductWithSuchIdException;

import java.util.List;

public interface ProductDao {

    Product get(Long id) throws NoProductWithSuchIdException;

    List<Product> find(String query, SortType sortType, OrderType orderType);

    void save(Product product);

    void delete(Long id) throws NoProductWithSuchIdException;
}
