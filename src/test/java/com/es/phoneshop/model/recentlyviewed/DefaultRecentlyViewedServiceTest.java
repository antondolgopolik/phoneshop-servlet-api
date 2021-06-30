package com.es.phoneshop.model.recentlyviewed;

import com.es.phoneshop.TestUtils;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;

import static com.es.phoneshop.model.product.HashMapProductDaoTest.initHashMapProductDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRecentlyViewedServiceTest {
    private DefaultRecentlyViewedService defaultRecentlyViewedService;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpSession httpSession;

    @Before
    public void setup() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Environment
        TestUtils.setupEnvironment();
        initHashMapProductDao(10);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        // Variables
        defaultRecentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Test
    public void testUpdate() {
        RecentlyViewed recentlyViewed = defaultRecentlyViewedService.getRecentlyViewed(httpServletRequest);
        Product[] products = ProductTest.getMockProducts(5);
        for (Product product : products) {
            defaultRecentlyViewedService.update(recentlyViewed, product);
        }
        assertEquals(recentlyViewed.getProducts().size(), 3);
    }

    @Test
    public void testUpdateNull() {
        boolean flag = false;
        RecentlyViewed recentlyViewed = defaultRecentlyViewedService.getRecentlyViewed(httpServletRequest);
        try {
            defaultRecentlyViewedService.update(recentlyViewed, null);
        } catch (IllegalArgumentException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }
}
