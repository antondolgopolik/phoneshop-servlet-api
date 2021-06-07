package com.es.phoneshop.web;

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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private RequestDispatcher requestDispatcher;

    private final ProductListPageServlet servlet = new ProductListPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init();
        when(request.getSession()).thenReturn(httpSession);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getParameter("query")).thenReturn("a");
        when(request.getParameter("sort")).thenReturn("description");
        when(request.getParameter("order")).thenReturn("des");
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNullQuery() throws ServletException, IOException {
        when(request.getParameter("query")).thenReturn(null);
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNullSortAndOrder() throws ServletException, IOException {
        when(request.getParameter("query")).thenReturn("");
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("order")).thenReturn(null);
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWrongSort() throws ServletException, IOException {
        when(request.getParameter("query")).thenReturn("");
        when(request.getParameter("sort")).thenReturn("123");
        when(request.getParameter("order")).thenReturn("des");
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWrongOrder() throws ServletException, IOException {
        when(request.getParameter("query")).thenReturn("");
        when(request.getParameter("sort")).thenReturn("description");
        when(request.getParameter("order")).thenReturn("123");
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }
}