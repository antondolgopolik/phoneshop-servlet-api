package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.ProductNotEnoughException;
import com.es.phoneshop.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.HashMapProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.recentlyviewed.DefaultRecentlyViewedService;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewed;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewedService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String QUANTITY_PARAM = "quantity";
    private static final String PRODUCT_ATTR = "product";
    private static final String CART_ATTR = "cart";
    private static final String RESULT_ATTR = "result";
    private static final String QUANTITY_ERROR_ATTR = "quantityError";

    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = HashMapProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read id
        long id = readId(request);
        // Add to recently viewed products
        RecentlyViewed recentlyViewed = recentlyViewedService.getRecentlyViewed(request);
        Product product = productDao.getProduct(id);
        recentlyViewedService.update(recentlyViewed, product);
        // Send response
        request.setAttribute(PRODUCT_ATTR, product);
        request.setAttribute(CART_ATTR, cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read params
        long id = readId(request);
        int quantity;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
            quantity = numberFormat.parse(request.getParameter(QUANTITY_PARAM)).intValue();
        } catch (ParseException e) {
            request.setAttribute(RESULT_ATTR, false);
            request.setAttribute(QUANTITY_ERROR_ATTR, "Number format exception");
            doGet(request, response);
            return;
        }
        // Add product to cart
        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, id, quantity);
        } catch (ProductNotEnoughException e) {
            request.setAttribute(RESULT_ATTR, false);
            request.setAttribute(QUANTITY_ERROR_ATTR, e.getMessage());
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
