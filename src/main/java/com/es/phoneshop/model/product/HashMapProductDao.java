package com.es.phoneshop.model.product;

import com.es.phoneshop.exceptions.NoProductWithSuchIdException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HashMapProductDao implements ProductDao {
    private final Map<Long, Product> products = new HashMap<>();
    private volatile long nextId;

    public static HashMapProductDao getInstance() {
        return SingletonInstanceHolder.instance;
    }

    private HashMapProductDao() {
    }

    @Override
    public Product getProduct(Long id) throws NoProductWithSuchIdException {
        // Find product
        Product product;
        synchronized (products) {
            product = products.get(id);
        }
        // Handle result
        if (product != null) {
            return product;
        } else {
            throw new NoProductWithSuchIdException(id);
        }
    }

    @Override
    public List<Product> findProducts(String query, SortType sortType, OrderType orderType) {
        synchronized (products) {
            Collection<Product> products = this.products.values();
            // General filtration
            Stream<Product> stream = products.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0);
            // Search
            if (query != null) {
                stream = search(stream, query);
            }
            // Sort
            if (sortType != null) {
                stream = sort(stream, sortType, orderType);
            }
            // Result
            return stream.collect(Collectors.toList());
        }
    }

    private Stream<Product> search(Stream<Product> stream, String query) {
        String[] queryWords = query.toLowerCase().split(" ");
        return searchSort(searchFilter(stream, queryWords), queryWords);
    }

    private Stream<Product> searchFilter(Stream<Product> stream, String[] queryWords) {
        return stream.filter(product -> {
            String description = product.getDescription().toLowerCase();
            return Arrays.stream(queryWords).anyMatch(description::contains);
        });
    }

    private Stream<Product> searchSort(Stream<Product> stream, String[] queryWords) {
        return stream.sorted((product1, product2) -> {
            long metric1 = findMatchMetric(product1, queryWords);
            long metric2 = findMatchMetric(product2, queryWords);
            return Long.compare(metric2 - metric1, 0);
        });
    }

    private long findMatchMetric(Product product, String[] queryWords) {
        String[] descriptionWords = product.getDescription().toLowerCase().split(" ");
        return Arrays.stream(descriptionWords)
                .filter(descriptionWord ->
                        Arrays.stream(queryWords).anyMatch(queryWord ->
                                descriptionWord.matches(".*" + Pattern.quote(queryWord) + ".*")
                        )
                )
                .count();
    }

    private Stream<Product> sort(Stream<Product> stream, SortType sortType, OrderType orderType) {
        if (sortType == SortType.DESCRIPTION) {
            if (orderType == OrderType.DESCENDING) {
                return stream.sorted(Comparator.comparing(Product::getDescription).reversed());
            } else {
                return stream.sorted(Comparator.comparing(Product::getDescription));
            }
        } else {
            if (orderType == OrderType.DESCENDING) {
                return stream.sorted(Comparator.comparing(Product::getPrice).reversed());
            } else {
                return stream.sorted(Comparator.comparing(Product::getPrice));
            }
        }
    }

    @Override
    public void save(Product product) {
        Long id = product.getId();
        if (id == null) {
            id = nextId();
            product.setId(id);
        }
        synchronized (products) {
            products.put(id, product);
        }
    }

    private synchronized long nextId() {
        while (products.containsKey(nextId)) {
            nextId++;
        }
        return nextId;
    }

    @Override
    public void delete(Long id) throws NoProductWithSuchIdException {
        // Delete product
        Product product;
        synchronized (products) {
            product = products.remove(id);
        }
        // Check result
        if (product == null) {
            throw new NoProductWithSuchIdException(id);
        }
    }

    private static class SingletonInstanceHolder {
        private static final HashMapProductDao instance = new HashMapProductDao();
    }
}
