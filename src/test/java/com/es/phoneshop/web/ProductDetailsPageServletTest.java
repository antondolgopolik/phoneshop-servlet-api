package com.es.phoneshop.web;

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
import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private RequestDispatcher requestDispatcher;

    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @BeforeClass
    public static void classSetup() {
        HashMapProductDaoTest.initHashMapProductDao(100);
    }

    @Before
    public void setup() throws ServletException {
        servlet.init();
        when(request.getSession()).thenReturn(httpSession);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        servlet.doGet(request, response);
        verify(request, never()).setAttribute("result", false);
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

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/50");
        when(request.getParameter("quantity")).thenReturn("3");
        when(request.getLocale()).thenReturn(Locale.US);
        servlet.doPost(request, response);
        verify(request, never()).setAttribute("result", false);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNonExistentId() throws ServletException, IOException {
        boolean flag = false;
        when(request.getPathInfo()).thenReturn("/1000");
        when(request.getParameter("quantity")).thenReturn("3");
        when(request.getLocale()).thenReturn(Locale.US);
        try {
            servlet.doPost(request, response);
        } catch (ProductNotFoundException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void testDoPostWrongId() throws ServletException, IOException {
        boolean flag = false;
        when(request.getPathInfo()).thenReturn("/abc");
        try {
            servlet.doPost(request, response);
        } catch (ProductNotFoundException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void testDoPostNonPositiveQuantity() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/50");
        when(request.getParameter("quantity")).thenReturn("0");
        when(request.getLocale()).thenReturn(Locale.US);
        servlet.doPost(request, response);
        verify(request).setAttribute("result", false);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostQuantityNotEnough() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/50");
        when(request.getParameter("quantity")).thenReturn("1000");
        when(request.getLocale()).thenReturn(Locale.US);
        servlet.doPost(request, response);
        verify(request).setAttribute("result", false);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostDecimalQuantity() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/50");
        when(request.getParameter("quantity")).thenReturn("3.5");
        when(request.getLocale()).thenReturn(Locale.US);
        servlet.doPost(request, response);
        verify(request).setAttribute("result", false);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWrongQuantity() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/50");
        when(request.getParameter("quantity")).thenReturn("abc");
        when(request.getLocale()).thenReturn(Locale.US);
        servlet.doPost(request, response);
        verify(request).setAttribute("result", false);
        verify(response, never()).sendRedirect(anyString());
    }
}
