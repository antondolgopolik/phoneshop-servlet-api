package com.es.phoneshop.model.order;

import com.es.phoneshop.TestUtils;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartTest;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.es.phoneshop.model.product.HashMapProductDaoTest.initHashMapProductDao;
import static org.junit.Assert.assertEquals;

public class DefaultOderServiceTest {
    private OrderService orderService;

    @Before
    public void setup() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Environment
        TestUtils.setupEnvironment();
        initHashMapProductDao(10);
        // Variables
        orderService = DefaultOrderService.getInstance();
    }

    @Test
    public void testGetOrder() {
        Cart cart = CartTest.getMockCart();
        Order order = orderService.getOrder(cart);
        assertEquals(cart.getTotalCost(), order.getCart().getTotalCost());
    }
}
