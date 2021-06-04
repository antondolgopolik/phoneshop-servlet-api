package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.HashMapProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

public class DefaultCartService implements CartService {
    private static volatile DefaultCartService instance;

    private final Cart cart = new Cart();
    private final ProductDao productDao = HashMapProductDao.getInstance();

    public static synchronized DefaultCartService getInstance() {
        if (instance == null) {
            instance = new DefaultCartService();
        }
        return instance;
    }

    private DefaultCartService() {
    }

    @Override
    public Cart getCart() {
        return null;
    }

    @Override
    public void add(Long productId, int quantity) {
        Product product = productDao.getProduct(productId);
        CartItem cartItem = new CartItem(product, quantity);
        cart.getItems().add(cartItem);
    }
}
