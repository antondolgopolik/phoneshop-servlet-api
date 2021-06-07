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
    private static final String DESCRIPTION_SORT = "description";
    private static final String PRICE_SORT = "price";
    private static final String DESCENDING_ORDER = "des";
    private static final String ASCENDING_ORDER = "asc";

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
        SortType sortType = readSortType(request);
        OrderType orderType = readOrderType(request);
        // Find products
        List<Product> products = productDao.findProducts(query, sortType, orderType);
        // Get recently viewed products
        RecentlyViewed recentlyViewed = recentlyViewedService.getRecentlyViewed(request);
        // Send response
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
}
