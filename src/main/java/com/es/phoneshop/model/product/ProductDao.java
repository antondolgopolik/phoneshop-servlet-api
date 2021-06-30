package com.es.phoneshop.model.product;

import com.es.phoneshop.exceptions.NoProductWithSuchIdException;
import com.es.phoneshop.model.advancedSearch.SearchMode;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {

    Product get(Long id) throws NoProductWithSuchIdException;

    List<Product> find(String query, SortType sortType, OrderType orderType);

    List<Product> find(String query, SearchMode searchMode, BigDecimal minPrice, BigDecimal maxPrice);

    void save(Product product);

    void delete(Long id) throws NoProductWithSuchIdException;
}
