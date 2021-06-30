package com.es.phoneshop.web;

import com.es.phoneshop.model.order.HashMapOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    private static final String ORDER_ATTR = "order";

    private OrderDao orderDao;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDao = HashMapOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read id
        String id = request.getPathInfo().substring(1);
        // Get order
        Order order = orderDao.get(id);
        // Send response
        request.setAttribute(ORDER_ATTR, order);
        request.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(request, response);
    }
}
