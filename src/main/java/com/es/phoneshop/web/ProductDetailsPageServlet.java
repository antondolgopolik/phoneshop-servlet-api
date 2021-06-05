package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.ProductNotEnoughException;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.HashMapProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = HashMapProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read id
        long id = readId(request);
        // Send response
        request.setAttribute("product", productDao.getProduct(id));
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read params
        long id = readId(request);
        int quantity;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
            quantity = numberFormat.parse(request.getParameter("quantity")).intValue();
        } catch (ParseException e) {
            request.setAttribute("result", false);
            request.setAttribute("quantityError", "Number format exception");
            doGet(request, response);
            return;
        }
        // Add product to cart
        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, id, quantity);
        } catch (ProductNotEnoughException e) {
            request.setAttribute("result", false);
            request.setAttribute("quantityError", e.getMessage());
            doGet(request, response);
            return;
        }
        // Send response
        response.sendRedirect(request.getContextPath() + "/products/" + id + "?result=" + true);
    }

    private long readId(HttpServletRequest request) {
        try {
            return Long.parseLong(request.getPathInfo().substring(1));
        } catch (NumberFormatException e) {
            throw new ProductNotFoundException();
        }
    }
}
