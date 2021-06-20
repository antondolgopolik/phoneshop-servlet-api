package com.es.phoneshop.web;

import com.es.phoneshop.exceptions.IllegalProductQuantityValueException;
import com.es.phoneshop.exceptions.ProductNotEnoughException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.*;
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
import java.util.List;

public class ProductListPageServlet extends HttpServlet {
    private static final String QUERY_PARAM = "query";
    private static final String SORT_PARAM = "sort";
    private static final String ORDER_PARAM = "order";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String CART_ATTR = "cart";
    private static final String PRODUCTS_ATTR = "products";
    private static final String RECENTLY_VIEWED_ATTR = "recentlyViewed";
    private static final String QUANTITY_ERROR_ATTR = "quantityError";
    private static final String DESCRIPTION_SORT = "description";
    private static final String PRICE_SORT = "price";
    private static final String DESCENDING_ORDER = "des";
    private static final String ASCENDING_ORDER = "asc";

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
        // Read params
        String query = request.getParameter(QUERY_PARAM);
        SortType sortType = readSortType(request);
        OrderType orderType = readOrderType(request);
        // Get cart
        Cart cart = cartService.getCart(request);
        // Find products
        List<Product> products = productDao.find(query, sortType, orderType);
        // Get recently viewed products
        RecentlyViewed recentlyViewed = recentlyViewedService.getRecentlyViewed(request);
        // Send response
        request.setAttribute(CART_ATTR, cart);
        request.setAttribute(PRODUCTS_ATTR, products);
        request.setAttribute(RECENTLY_VIEWED_ATTR, recentlyViewed);
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    private SortType readSortType(HttpServletRequest request) {
        String sort = request.getParameter(SORT_PARAM);
        if (sort != null) {
            return sort.equals(DESCRIPTION_SORT)
                    ? SortType.DESCRIPTION
                    : sort.equals(PRICE_SORT) ? SortType.PRICE : null;
        }
        return null;
    }

    private OrderType readOrderType(HttpServletRequest request) {
        String order = request.getParameter(ORDER_PARAM);
        if (order != null) {
            return order.equals(DESCENDING_ORDER)
                    ? OrderType.DESCENDING
                    : order.equals(ASCENDING_ORDER) ? OrderType.ASCENDING : null;
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read params
        long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
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
                request.getContextPath() + "/products?status=Product addition to cart succeeded"
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
