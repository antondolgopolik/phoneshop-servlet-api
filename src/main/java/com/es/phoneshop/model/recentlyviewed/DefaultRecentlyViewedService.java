package com.es.phoneshop.model.recentlyviewed;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Queue;

public class DefaultRecentlyViewedService implements RecentlyViewedService {
    private static final String RECENTLY_VIEWED_SESSION_ID =
            DefaultRecentlyViewedService.class.getName() + ".recentlyViewed";

    private static final int RECENTLY_VIEWED_SIZE = 3;

    public static DefaultRecentlyViewedService getInstance() {
        return SingletonInstanceHolder.instance;
    }

    private DefaultRecentlyViewedService() {
    }

    @Override
    public RecentlyViewed getRecentlyViewed(HttpServletRequest httpServletRequest) {
        synchronized (httpServletRequest.getSession()) {
            HttpSession httpSession = httpServletRequest.getSession();
            RecentlyViewed recentlyViewed = (RecentlyViewed) httpSession.getAttribute(RECENTLY_VIEWED_SESSION_ID);
            if (recentlyViewed == null) {
                recentlyViewed = new RecentlyViewed();
                httpSession.setAttribute(RECENTLY_VIEWED_SESSION_ID, recentlyViewed);
            }
            return recentlyViewed;
        }
    }

    @Override
    public void update(RecentlyViewed recentlyViewed, Product product) {
        synchronized (recentlyViewed.getProducts()) {
            Queue<Product> products = recentlyViewed.getProducts();
            if (products.parallelStream().noneMatch(p -> p.equals(product))) {
                if (products.size() == RECENTLY_VIEWED_SIZE) {
                    products.poll();
                }
                products.add(product);
            }
        }
    }

    private static class SingletonInstanceHolder {
        private static final DefaultRecentlyViewedService instance = new DefaultRecentlyViewedService();
    }
}
