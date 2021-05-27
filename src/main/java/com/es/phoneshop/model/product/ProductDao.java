package com.es.phoneshop.model.product;

import com.es.phoneshop.exceptions.NoProductWithSuchIdException;

import java.util.List;

public interface ProductDao {

    Product getProduct(Long id) throws NoProductWithSuchIdException;

    List<Product> findProducts(String query);

    void save(Product product);

    void delete(Long id) throws NoProductWithSuchIdException;
}
