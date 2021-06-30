package com.es.phoneshop.web;

import com.es.phoneshop.TestUtils;
import com.es.phoneshop.exceptions.OrderNotFoundException;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.order.HashMapOrderDaoTest;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.HashMapProductDaoTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private RequestDispatcher requestDispatcher;

    private OrderOverviewPageServlet servlet;
    private Order[] orders;

    @Before
    public void setup() throws ServletException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Environment
        TestUtils.setupEnvironment();
        HashMapProductDaoTest.initHashMapProductDao(10);
        orders = HashMapOrderDaoTest.initHashMapOrderDao(10);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        // Variables
        servlet = new OrderOverviewPageServlet();
        servlet.init();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/" + orders[0].getId());
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNonExistentId() throws ServletException, IOException {
        boolean flag = false;
        when(request.getPathInfo()).thenReturn("/abc");
        try {
            servlet.doGet(request, response);
        } catch (OrderNotFoundException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void testDoGetWithoutId() throws ServletException, IOException {
        boolean flag = false;
        when(request.getPathInfo()).thenReturn("/");
        try {
            servlet.doGet(request, response);
        } catch (OrderNotFoundException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }
}
