package com.es.phoneshop.model.order;

import com.es.phoneshop.TestUtils;
import com.es.phoneshop.exceptions.NoProductWithSuchIdException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;

import static com.es.phoneshop.model.product.HashMapProductDaoTest.initHashMapProductDao;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class HashMapOrderDaoTest {
    private OrderDao orderDao;
    private Order[] orders;

    public static Order[] initHashMapOrderDao(int n) {
        HashMapOrderDao hashMapOrderDao = HashMapOrderDao.getInstance();
        Order[] orders = OrderTest.getMockOrders(n);
        for (Order order : orders) {
            hashMapOrderDao.save(order);
        }
        return orders;
    }

    @Before
    public void setup() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Environment
        TestUtils.setupEnvironment();
        initHashMapProductDao(10);
        orders = initHashMapOrderDao(10);
        // Variables
        orderDao = HashMapOrderDao.getInstance();
    }

    @Test
    public void testGetProduct() throws NoProductWithSuchIdException {
        Order order1 = orderDao.get(orders[0].getId());
        assertEquals(order1.getId(), orders[0].getId());
        Order order2 = orderDao.get(orders[7].getId());
        assertEquals(order2.getId(), orders[7].getId());
        Order order3 = orderDao.get(orders[9].getId());
        assertEquals(order3.getId(), orders[9].getId());
    }
}
