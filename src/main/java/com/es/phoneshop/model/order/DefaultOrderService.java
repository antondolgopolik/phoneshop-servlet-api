package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.DefaultCartService;

public class DefaultOrderService implements OrderService {

    public static DefaultOrderService getInstance() {
        return SingletonInstanceHolder.instance;
    }

    private DefaultOrderService() {
    }

    @Override
    public Order getOrder(Cart cart) {
        return new Order(DefaultCartService.getInstance().copyCart(cart));
    }

    @Override
    public PaymentMethod[] getPaymentMethods() {
        return PaymentMethod.values();
    }

    private static class SingletonInstanceHolder {
        private static final DefaultOrderService instance = new DefaultOrderService();
    }
}
