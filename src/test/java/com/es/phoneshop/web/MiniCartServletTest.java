package com.es.phoneshop.web;

import com.es.phoneshop.TestUtils;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private RequestDispatcher requestDispatcher;

    private MiniCartServlet servlet;

    @Before
    public void setup() throws ServletException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Environment
        TestUtils.setupEnvironment();
        HashMapProductDaoTest.initHashMapProductDao(10);
        when(request.getSession()).thenReturn(httpSession);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        // Variables
        servlet = new MiniCartServlet();
        servlet.init();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).include(request, response);
    }
}
