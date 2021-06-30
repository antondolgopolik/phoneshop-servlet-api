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
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private RequestDispatcher requestDispatcher;

    private CartPageServlet servlet;

    @Before
    public void setup() throws ServletException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Environment
        TestUtils.setupEnvironment();
        HashMapProductDaoTest.initHashMapProductDao(10);
        when(request.getSession()).thenReturn(httpSession);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        // Variables
        servlet = new CartPageServlet();
        servlet.init();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostDifferentLength() throws ServletException, IOException {
        when(request.getParameterValues("id")).thenReturn(new String[]{"1", "2", "3", "4", "5"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1", "2", "3", "4"});
        servlet.doPost(request, response);
        verify(response).sendError(400);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNonExistentId() throws ServletException, IOException {
        when(request.getParameterValues("id")).thenReturn(new String[]{"1", "2", "123", "4", "5"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1", "2", "3", "4", "5"});
        servlet.doPost(request, response);
        verify(response).sendError(400);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWrongId() throws ServletException, IOException {
        when(request.getParameterValues("id")).thenReturn(new String[]{"1", "2", "asd", "4", "5"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1", "2", "3", "4", "5"});
        servlet.doPost(request, response);
        verify(response).sendError(400);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWrongQuantity() throws ServletException, IOException {
        when(request.getParameterValues("id")).thenReturn(new String[]{"1", "2", "3", "4", "5"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1", "sd", "3", "4", "5"});
        servlet.doPost(request, response);
        verify(response).sendError(400);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNegativeQuantity() throws ServletException, IOException {
        when(request.getParameterValues("id")).thenReturn(new String[]{"1", "2", "3", "4", "5"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1", "-2", "3", "4", "5"});
        servlet.doPost(request, response);
        verify(response).sendError(400);
        verify(response, never()).sendRedirect(anyString());
    }
}
