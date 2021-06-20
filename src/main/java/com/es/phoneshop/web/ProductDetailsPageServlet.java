package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.IllegalProductQuantityValueException;
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
        long id;
        try {
            id = Long.parseLong(request.getPathInfo().substring(1));
        } catch (NumberFormatException e) {
            throw new ProductNotFoundException();
        }
        // Add to recently viewed products
        RecentlyViewed recentlyViewed = recentlyViewedService.getRecentlyViewed(request);
        Product product = productDao.get(id);
        recentlyViewedService.update(recentlyViewed, product);
        // Send response
        request.setAttribute(PRODUCT_ATTR, product);
        request.setAttribute(CART_ATTR, cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read params
        long id;
        try {
            id = Long.parseLong(request.getPathInfo().substring(1));
        } catch (NumberFormatException e) {
            response.sendError(400);
            return;
        }
        int quantity;
        try {
            quantity = readQuantity(request);
        } catch (ParseException e) {
            sendResponseWithQuantityErrorMessage(request, response, "Number format exception");
            return;
        } catch (IllegalProductQuantityValueException e) {
            sendResponseWithQuantityErrorMessage(request, response, e.getMessage());
            return;
        }
        // Add product to cart
        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, id, quantity);
        } catch (IllegalProductQuantityValueException | ProductNotEnoughException e) {
            sendResponseWithQuantityErrorMessage(request, response, e.getMessage());
            return;
        }
        // Send response
        response.sendRedirect(
                request.getContextPath() + "/products/" + id + "?status=Product addition to cart succeeded"
        );
    }

    private int readQuantity(HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        Number number = numberFormat.parse(request.getParameter(QUANTITY_PARAM));
        if (number.doubleValue() == number.intValue()) {
            return number.intValue();
        } else {
            throw new IllegalProductQuantityValueException();
        }
    }

    private void sendResponseWithQuantityErrorMessage(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      String message)
            throws ServletException, IOException {
        request.setAttribute(QUANTITY_ERROR_ATTR, message);
        doGet(request, response);
    }
}
