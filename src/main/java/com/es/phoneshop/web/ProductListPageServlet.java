package com.es.phoneshop.web;

import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.recentlyviewed.DefaultRecentlyViewedService;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewed;
import com.es.phoneshop.model.recentlyviewed.RecentlyViewedService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductListPageServlet extends HttpServlet {
    private static final String QUERY_PARAM = "query";
    private static final String SORT_PARAM = "sort";
    private static final String ORDER_PARAM = "order";
    private static final String PRODUCTS_ATTR = "products";
    private static final String RECENTLY_VIEWED_ATTR = "recentlyViewed";

    private ProductDao productDao;
    private RecentlyViewedService recentlyViewedService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = HashMapProductDao.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read params
        String query = request.getParameter(QUERY_PARAM);
        String sort = request.getParameter(SORT_PARAM);
        String order = request.getParameter(ORDER_PARAM);
        SortType sortType = sort != null
                ? sort.equals("description") ? SortType.DESCRIPTION : SortType.PRICE
                : null;
        OrderType orderType = order != null
                ? order.equals("des") ? OrderType.DESCENDING : OrderType.ASCENDING
                : null;
        // Find products
        List<Product> products = productDao.findProducts(query, sortType, orderType);
        // Get recently viewed products
        RecentlyViewed recentlyViewed = recentlyViewedService.getRecentlyViewed(request);
        // Send response
        request.setAttribute(PRODUCTS_ATTR, products);
        request.setAttribute(RECENTLY_VIEWED_ATTR, recentlyViewed);
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
