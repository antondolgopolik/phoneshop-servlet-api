package com.es.phoneshop.web;

import com.es.phoneshop.TestUtils;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private RequestDispatcher requestDispatcher;

    private CheckoutPageServlet servlet;

    @Before
    public void setup() throws ServletException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Environment
        TestUtils.setupEnvironment();
        HashMapProductDaoTest.initHashMapProductDao(10);
        when(request.getSession()).thenReturn(httpSession);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        // Variables
        servlet = new CheckoutPageServlet();
        servlet.init();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phone")).thenReturn("+123456789012");
        when(request.getParameter("deliveryDate")).thenReturn("2020-01-01");
        when(request.getParameter("deliveryAddress")).thenReturn("address");
        when(request.getParameter("paymentMethod")).thenReturn("Cash");
        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNoFirstName() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phone")).thenReturn("+123456789012");
        when(request.getParameter("deliveryDate")).thenReturn("2020-01-01");
        when(request.getParameter("deliveryAddress")).thenReturn("address");
        when(request.getParameter("paymentMethod")).thenReturn("Cash");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNoLastName() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("");
        when(request.getParameter("phone")).thenReturn("+123456789012");
        when(request.getParameter("deliveryDate")).thenReturn("2020-01-01");
        when(request.getParameter("deliveryAddress")).thenReturn("address");
        when(request.getParameter("paymentMethod")).thenReturn("Cash");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNoPhone() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phone")).thenReturn("");
        when(request.getParameter("deliveryDate")).thenReturn("2020-01-01");
        when(request.getParameter("deliveryAddress")).thenReturn("address");
        when(request.getParameter("paymentMethod")).thenReturn("Cash");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWrongDeliveryDate() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phone")).thenReturn("+123456789012");
        when(request.getParameter("deliveryDate")).thenReturn("abc");
        when(request.getParameter("deliveryAddress")).thenReturn("address");
        when(request.getParameter("paymentMethod")).thenReturn("Cash");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNoDeliveryDate() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phone")).thenReturn("+123456789012");
        when(request.getParameter("deliveryDate")).thenReturn("");
        when(request.getParameter("deliveryAddress")).thenReturn("address");
        when(request.getParameter("paymentMethod")).thenReturn("Cash");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNoDeliveryAddress() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phone")).thenReturn("+123456789012");
        when(request.getParameter("deliveryDate")).thenReturn("2020-01-01");
        when(request.getParameter("deliveryAddress")).thenReturn("");
        when(request.getParameter("paymentMethod")).thenReturn("Cash");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWrongPaymentMethod() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phone")).thenReturn("+123456789012");
        when(request.getParameter("deliveryDate")).thenReturn("2020-01-01");
        when(request.getParameter("deliveryAddress")).thenReturn("address");
        when(request.getParameter("paymentMethod")).thenReturn("paymentMethod");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNoPaymentMethod() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phone")).thenReturn("+123456789012");
        when(request.getParameter("deliveryDate")).thenReturn("2020-01-01");
        when(request.getParameter("deliveryAddress")).thenReturn("address");
        when(request.getParameter("paymentMethod")).thenReturn("");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}
