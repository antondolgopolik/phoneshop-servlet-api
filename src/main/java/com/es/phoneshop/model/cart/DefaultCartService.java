package com.es.phoneshop.model.cart;

import com.es.phoneshop.exceptions.ProductNotEnoughException;
import com.es.phoneshop.model.product.HashMapProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ID = DefaultCartService.class.getName() + ".cart";

    private final ProductDao productDao = HashMapProductDao.getInstance();

    public static DefaultCartService getInstance() {
        return SingletonInstanceHolder.instance;
    }

    private DefaultCartService() {
    }

    @Override
    public Cart getCart(HttpServletRequest httpServletRequest) {
        synchronized (httpServletRequest.getSession()) {
            HttpSession httpSession = httpServletRequest.getSession();
            Cart cart = (Cart) httpSession.getAttribute(CART_SESSION_ID);
            if (cart == null) {
                cart = new Cart();
                httpSession.setAttribute(CART_SESSION_ID, cart);
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws ProductNotEnoughException {
        synchronized (cart.getItems()) {
            // Try to find cart item
            List<CartItem> items = cart.getItems();
            Product product = productDao.getProduct(productId);
            CartItem cartItem = items.parallelStream()
                    .filter(item -> item.getProduct().equals(product))
                    .findAny().orElse(null);
            // Check if cart item was found
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

    private static class SingletonInstanceHolder {
        private static final DefaultCartService instance = new DefaultCartService();
    }
}
