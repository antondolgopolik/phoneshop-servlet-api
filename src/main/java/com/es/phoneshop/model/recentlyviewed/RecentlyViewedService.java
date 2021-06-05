package com.es.phoneshop.model.recentlyviewed;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;

public interface RecentlyViewedService {

    RecentlyViewed getRecentlyViewed(HttpServletRequest httpServletRequest);

    void update(RecentlyViewed recentlyViewed, Product product);
}
