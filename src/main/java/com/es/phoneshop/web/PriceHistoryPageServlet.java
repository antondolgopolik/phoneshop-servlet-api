package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.product.HashMapProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PriceHistoryPageServlet extends HttpServlet {
    private static final String PRODUCT_ATTR = "product";

    private ProductDao productDao;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = HashMapProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read id
        long id = readId(request);
        // Send response
        request.setAttribute(PRODUCT_ATTR, productDao.get(id));
        request.getRequestDispatcher("/WEB-INF/pages/priceHistory.jsp").forward(request, response);
    }

    private long readId(HttpServletRequest request) {
        try {
            return Long.parseLong(request.getPathInfo().substring(1));
        } catch (NumberFormatException e) {
            throw new ProductNotFoundException();
        }
    }
}
