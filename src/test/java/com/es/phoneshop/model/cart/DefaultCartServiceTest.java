package com.es.phoneshop.model.cart;

import com.es.phoneshop.exceptions.IllegalProductQuantityValueException;
import com.es.phoneshop.exceptions.ProductNotEnoughException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.es.phoneshop.model.product.HashMapProductDaoTest.initHashMapProductDao;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    private final DefaultCartService defaultCartService = DefaultCartService.getInstance();

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpSession httpSession;

    @BeforeClass
    public static void classSetup() {
        initHashMapProductDao(100);
    }

    @Before
    public void setup() {
        when(httpServletRequest.getSession()).thenReturn(httpSession);
    }

    @Test
    public void testGetCart() {
        assertNotNull(defaultCartService.getCart(httpServletRequest));
    }

    @Test
    public void testAdd() throws ProductNotEnoughException {
        Cart cart = defaultCartService.getCart(httpServletRequest);
        for (int i = 1; i < 10; i++) {
            defaultCartService.add(cart, (long) i, i);
        }
        assertEquals(cart.getItems().size(), 9);
    }

    @Test
    public void testAddNegative() {
        boolean flag = false;
        Cart cart = defaultCartService.getCart(httpServletRequest);
        try {
            defaultCartService.add(cart, 0L, 0);
        } catch (IllegalProductQuantityValueException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void testAddQualitySum() throws ProductNotEnoughException {
        Cart cart = defaultCartService.getCart(httpServletRequest);
        defaultCartService.add(cart, 9L, 2);
        defaultCartService.add(cart, 9L, 3);
        assertEquals(cart.getItems().get(0).getQuantity(), 5);
    }

    @Test
    public void testAddStockNotEnough() {
        boolean flag = false;
        Cart cart = defaultCartService.getCart(httpServletRequest);
        try {
            defaultCartService.add(cart, 1L, 10);
        } catch (ProductNotEnoughException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }
}
