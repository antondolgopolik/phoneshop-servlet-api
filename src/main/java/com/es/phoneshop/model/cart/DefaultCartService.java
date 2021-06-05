package com.es.phoneshop.model.cart;

import com.es.phoneshop.exceptions.ProductNotEnoughException;
import com.es.phoneshop.model.product.HashMapProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import java.util.List;

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
        return cart;
    }

    @Override
    public void add(Long productId, int quantity) throws ProductNotEnoughException {
        // Try to find cart item
        List<CartItem> items = cart.getItems();
        Product product = productDao.getProduct(productId);
        CartItem cartItem = items.parallelStream()
                .filter(item -> item.getProduct().equals(product))
                .findAny().orElse(null);
        // Check if cart item was founded
        if (cartItem != null) {
            int resultQuantity = cartItem.getQuantity() + quantity;
            // Check stock
            if (resultQuantity > product.getStock()) {
                throw new ProductNotEnoughException(product.getStock() - cartItem.getQuantity());
            } else {
                cartItem.setQuantity(resultQuantity);
            }
        } else {
            // Check stock
            if (quantity > product.getStock()) {
                throw new ProductNotEnoughException(product.getStock());
            } else {
                items.add(new CartItem(product, quantity));
            }
        }
    }
}
