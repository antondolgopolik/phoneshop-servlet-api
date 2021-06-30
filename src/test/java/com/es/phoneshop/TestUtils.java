package com.es.phoneshop;

import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.HashMapOrderDao;
import com.es.phoneshop.model.product.HashMapProductDao;
import com.es.phoneshop.model.recentlyviewed.DefaultRecentlyViewedService;
import com.es.phoneshop.security.DefaultDosProtectionService;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TestUtils {
    private static final MockedStatic<HashMapOrderDao> HASH_MAP_ORDER_DAO =
            Mockito.mockStatic(HashMapOrderDao.class);
    private static final MockedStatic<HashMapProductDao> HASH_MAP_PRODUCT_DAO =
            Mockito.mockStatic(HashMapProductDao.class);
    private static final MockedStatic<DefaultCartService> DEFAULT_CART_SERVICE =
            Mockito.mockStatic(DefaultCartService.class);
    private static final MockedStatic<DefaultOrderService> DEFAULT_ORDER_SERVICE =
            Mockito.mockStatic(DefaultOrderService.class);
    private static final MockedStatic<DefaultRecentlyViewedService> DEFAULT_RECENTLY_VIEWED_SERVICE =
            Mockito.mockStatic(DefaultRecentlyViewedService.class);
    private static final MockedStatic<DefaultDosProtectionService> DEFAULT_DOS_PROTECTION_SERVICE =
            Mockito.mockStatic(DefaultDosProtectionService.class);

    public static void setupEnvironment() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var hashMapOrderDao = newInstance(HashMapOrderDao.class);
        HASH_MAP_ORDER_DAO.when(HashMapOrderDao::getInstance).thenReturn(hashMapOrderDao);
        var hashMapProductDao = newInstance(HashMapProductDao.class);
        HASH_MAP_PRODUCT_DAO.when(HashMapProductDao::getInstance).thenReturn(hashMapProductDao);
        var defaultCartService = newInstance(DefaultCartService.class);
        DEFAULT_CART_SERVICE.when(DefaultCartService::getInstance).thenReturn(defaultCartService);
        var defaultOrderService = newInstance(DefaultOrderService.class);
        DEFAULT_ORDER_SERVICE.when(DefaultOrderService::getInstance).thenReturn(defaultOrderService);
        var defaultRecentlyViewedService = newInstance(DefaultRecentlyViewedService.class);
        DEFAULT_RECENTLY_VIEWED_SERVICE.when(DefaultRecentlyViewedService::getInstance).thenReturn(defaultRecentlyViewedService);
        var dosProtectionService = newInstance(DefaultDosProtectionService.class);
        DEFAULT_DOS_PROTECTION_SERVICE.when(DefaultDosProtectionService::getInstance).thenReturn(dosProtectionService);
    }

    private static <T> T newInstance(Class<T> aClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<T> constructor = aClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}
