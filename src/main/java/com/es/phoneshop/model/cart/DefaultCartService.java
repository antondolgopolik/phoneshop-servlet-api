package com.es.phoneshop.model.cart;

import com.es.phoneshop.exceptions.CartItemNotFoundException;
import com.es.phoneshop.exceptions.IllegalProductQuantityValueException;
import com.es.phoneshop.exceptions.NoProductWithSuchIdException;
import com.es.phoneshop.exceptions.ProductNotEnoughException;
import com.es.phoneshop.model.product.HashMapProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Iterator;
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
    public Cart copyCart(Cart cart) {
        synchronized (cart.getItems()) {
            return new Cart(cart);
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity)
            throws IllegalProductQuantityValueException, NoProductWithSuchIdException, ProductNotEnoughException {
        if (quantity < 1) {
            throw new IllegalProductQuantityValueException();
        }
        synchronized (cart.getItems()) {
            // Try to find cart item
            List<CartItem> items = cart.getItems();
            Product product = productDao.get(productId);
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
                    updateTotalCostAndQuantity(cart, cartItem, quantity);
                    cartItem.setQuantity(resultQuantity);
                }
            } else {
                // Check stock
                if (quantity > product.getStock()) {
                    throw new ProductNotEnoughException(product.getStock());
                } else {
                    cartItem = new CartItem(product, quantity);
                    updateTotalCostAndQuantity(cart, cartItem, quantity);
                    items.add(cartItem);
                }
            }
        }
    }

    @Override
    public void delete(Cart cart, Long productId) throws CartItemNotFoundException {
        synchronized (cart.getItems()) {
            Iterator<CartItem> iterator = cart.getItems().iterator();
            while (iterator.hasNext()) {
                CartItem cartItem = iterator.next();
                if (cartItem.getProduct().getId().equals(productId)) {
                    updateTotalCostAndQuantity(cart, cartItem, -cartItem.getQuantity());
                    iterator.remove();
                    return;
                }
            }
            throw new CartItemNotFoundException(productId);
        }
    }

    @Override
    public void clear(Cart cart) {
        synchronized (cart.getItems()) {
            cart.getItems().clear();
            cart.setTotalCost(BigDecimal.ZERO);
            cart.setTotalQuantity(0);
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity)
            throws IllegalProductQuantityValueException, NoProductWithSuchIdException, CartItemNotFoundException,
            ProductNotEnoughException {
        if (quantity < 1) {
            throw new IllegalProductQuantityValueException();
        }
        synchronized (cart.getItems()) {
            // Find cart item
            List<CartItem> items = cart.getItems();
            Product product = productDao.get(productId);
            CartItem cartItem = items.parallelStream()
                    .filter(item -> item.getProduct().equals(product))
                    .findAny().orElse(null);
            // Check if cart item was found
            if (cartItem != null) {
                // Check stock
                if (quantity > product.getStock()) {
                    throw new ProductNotEnoughException(product.getStock());
                } else {
                    updateTotalCostAndQuantity(cart, cartItem, quantity - cartItem.getQuantity());
                    cartItem.setQuantity(quantity);
                }
            } else {
                throw new CartItemNotFoundException(productId);
            }
        }
    }

    private void updateTotalCostAndQuantity(Cart cart, CartItem cartItem, int delta) {
        updateTotalCost(cart, cartItem, delta);
        updateTotalQuantity(cart, delta);
    }

    private void updateTotalCost(Cart cart, CartItem cartItem, int delta) {
        BigDecimal totalCost = cart.getTotalCost();
        BigDecimal price = cartItem.getProduct().getPrice();
        BigDecimal newTotalCost = totalCost.add(price.multiply(BigDecimal.valueOf(delta)));
        cart.setTotalCost(newTotalCost);
    }

    private void updateTotalQuantity(Cart cart, int delta) {
        cart.setTotalQuantity(cart.getTotalQuantity() + delta);
    }

    private static class SingletonInstanceHolder {
        private static final DefaultCartService instance = new DefaultCartService();
    }
}
