package com.es.phoneshop.model.product;

import com.es.phoneshop.exceptions.NoProductWithSuchIdException;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao implements ProductDao {
    private final Map<Long, Product> products = new HashMap<>();
    private volatile long nextId;

    public ArrayListProductDao() {
        initProducts();
    }

    private void initProducts() {
        Currency usd = Currency.getInstance("USD");
        save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
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
    public List<Product> findProducts(String query) {
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
}
