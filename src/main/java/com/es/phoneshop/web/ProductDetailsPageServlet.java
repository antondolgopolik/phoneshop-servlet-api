package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read id
        long id;
        try {
            id = Long.parseLong(request.getPathInfo().substring(1));
        } catch (NumberFormatException e) {
            throw new ProductNotFoundException();
        }
        // Send response
        request.setAttribute("product", productDao.getProduct(id));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }
}
