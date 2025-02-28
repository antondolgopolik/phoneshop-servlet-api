package com.es.phoneshop.web;

import com.es.phoneshop.TestUtils;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.product.HashMapProductDaoTest;
import org.junit.Before;
import org.junit.BeforeClass;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PriceHistoryPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private RequestDispatcher requestDispatcher;

    private PriceHistoryPageServlet servlet;

    @Before
    public void setup() throws ServletException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Environment
        TestUtils.setupEnvironment();
        HashMapProductDaoTest.initHashMapProductDao(10);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        // Variables
        servlet = new PriceHistoryPageServlet();
        servlet.init();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNonExistentId() throws ServletException, IOException {
        boolean flag = false;
        when(request.getPathInfo()).thenReturn("/1000");
        try {
            servlet.doGet(request, response);
        } catch (ProductNotFoundException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void testDoGetWrongId() throws ServletException, IOException {
        boolean flag = false;
        when(request.getPathInfo()).thenReturn("/abc");
        try {
            servlet.doGet(request, response);
        } catch (ProductNotFoundException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }
}
