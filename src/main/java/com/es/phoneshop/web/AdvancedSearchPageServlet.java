package com.es.phoneshop.web;

import com.es.phoneshop.model.advancedSearch.AdvancedSearchService;
import com.es.phoneshop.model.advancedSearch.SearchMode;
import com.es.phoneshop.model.product.HashMapProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private static final String QUERY_PARAM = "query";
    private static final String SEARCH_MODE_PARAM = "searchMode";
    private static final String MIN_PRICE_PARAM = "minPrice";
    private static final String MAX_PRICE_PARAM = "maxPrice";
    private static final String PRODUCTS_ATTR = "products";
    private static final String SEARCH_MODES_ATTR = "searchModes";
    private static final String ERRORS_ATTR = "errors";

    private ProductDao productDao;
    private AdvancedSearchService advancedSearchService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = HashMapProductDao.getInstance();
        advancedSearchService = AdvancedSearchService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        // Read params
        String query = request.getParameter(QUERY_PARAM);
        SearchMode searchMode = readSearchMode(request, errors);
        BigDecimal minPrice = readPrice(request, MIN_PRICE_PARAM, errors);
        BigDecimal maxPrice = readPrice(request, MAX_PRICE_PARAM, errors);
        // Find products
        List<Product> products = productDao.find(query, searchMode, minPrice, maxPrice);
        // Send response
        request.setAttribute(PRODUCTS_ATTR, products);
        request.setAttribute(SEARCH_MODES_ATTR, advancedSearchService.getSearchModes());
        if ((query != null) || (searchMode != null) || (minPrice != null) || (maxPrice != null)) {
            if (!errors.isEmpty()) {
                request.setAttribute("status", "Product search failed");
            }
            request.setAttribute(ERRORS_ATTR, errors);
        }
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    private String readRequiredFieldParameter(HttpServletRequest request, String parameter, Map<String, String> errors) {
        String value = request.getParameter(parameter);
        if ((value == null) || value.isBlank()) {
            errors.put(parameter, "The field is required");
            return null;
        } else {
            return value;
        }
    }

    private SearchMode readSearchMode(HttpServletRequest request, Map<String, String> errors) {
        String searchModeParameter = readRequiredFieldParameter(request, SEARCH_MODE_PARAM, errors);
        SearchMode[] searchModes = SearchMode.values();
        for (SearchMode searchMode : searchModes) {
            if (searchMode.getSearchMode().equals(searchModeParameter)) {
                return searchMode;
            }
        }
        errors.put("searchMode", "Wrong search mode");
        return null;
    }

    private BigDecimal readPrice(HttpServletRequest request, String parameter, Map<String, String> errors) {
        String value = request.getParameter(parameter);
        if ((value != null) && !value.isBlank()) {
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e) {
                errors.put(parameter, "Wrong price format");
            }
        }
        return null;
    }
}
