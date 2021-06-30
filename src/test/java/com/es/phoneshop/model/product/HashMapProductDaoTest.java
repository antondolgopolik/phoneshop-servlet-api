package com.es.phoneshop.model.product;

import com.es.phoneshop.TestUtils;
import com.es.phoneshop.exceptions.NoProductWithSuchIdException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class HashMapProductDaoTest {
    private ProductDao productDao;

    public static void initHashMapProductDao(int n) {
        HashMapProductDao hashMapProductDao = HashMapProductDao.getInstance();
        Product[] products = ProductTest.getMockProducts(n);
        for (Product product : products) {
            hashMapProductDao.save(product);
        }
    }

    @Before
    public void setup() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Environment
        TestUtils.setupEnvironment();
        initHashMapProductDao(10);
        // Variables
        productDao = HashMapProductDao.getInstance();
    }

    @Test
    public void testGetProduct() throws NoProductWithSuchIdException {
        Product product1 = productDao.get(0L);
        assertEquals((long) product1.getId(), 0L);
        Product product2 = productDao.get(7L);
        assertEquals((long) product2.getId(), 7L);
        Product product3 = productDao.get(9L);
        assertEquals((long) product3.getId(), 9L);
    }

    @Test
    public void testFindProductsNullQuery() {
        assertFalse(productDao.find(null, SortType.DESCRIPTION, OrderType.DESCENDING).isEmpty());
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.find("", SortType.PRICE, OrderType.ASCENDING).isEmpty());
    }

    @Test
    public void testFindProductsGeneralFilter() {
        List<Product> products1 = productDao.find("", SortType.DESCRIPTION, OrderType.ASCENDING);
        List<Product> products2 = products1.stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
        assertEquals(products1, products2);
    }

    @Test
    public void testFindProductsDescriptionOrder() {
        List<Product> products1 = productDao.find("", SortType.DESCRIPTION, OrderType.DESCENDING);
        List<Product> products2 = productDao.find("", SortType.DESCRIPTION, OrderType.ASCENDING);
        assertEquals(products1.size(), products2.size());
        Collections.reverse(products2);
        Iterator<Product> iterator1 = products1.iterator();
        Iterator<Product> iterator2 = products2.iterator();
        while (iterator1.hasNext()) {
            assertEquals(iterator1.next().getPrice(), iterator2.next().getPrice());
        }
    }

    @Test
    public void testFindProductsPriceOrder() {
        List<Product> products1 = productDao.find("", SortType.PRICE, OrderType.DESCENDING);
        List<Product> products2 = productDao.find("", SortType.PRICE, OrderType.ASCENDING);
        assertEquals(products1.size(), products2.size());
        Collections.reverse(products2);
        Iterator<Product> iterator1 = products1.iterator();
        Iterator<Product> iterator2 = products2.iterator();
        while (iterator1.hasNext()) {
            assertEquals(iterator1.next().getPrice(), iterator2.next().getPrice());
        }
    }

    @Test
    public void testSave() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product(
                20L, "code1", "des1", BigDecimal.ZERO, usd, 1, "url1"
        );
        Product product2 = new Product(
                20L, "code2", "des2", BigDecimal.ZERO, usd, 2, "url2"
        );
        productDao.save(product1);
        assertEquals((long) productDao.get(20L).getId(), 20);
        assertEquals(product1, productDao.get(20L));
        productDao.save(product2);
        assertEquals((long) productDao.get(20L).getId(), 20);
        assertEquals(product2, productDao.get(20L));
    }

    @Test
    public void testDelete() {
        boolean flag = false;
        productDao.delete(0L);
        try {
            productDao.get(0L);
        } catch (NoProductWithSuchIdException ignored) {
            flag = true;
        }
        assertTrue(flag);

        flag = false;
        productDao.delete(7L);
        try {
            productDao.get(7L);
        } catch (NoProductWithSuchIdException ignored) {
            flag = true;
        }
        assertTrue(flag);

        flag = false;
        productDao.delete(9L);
        try {
            productDao.get(9L);
        } catch (NoProductWithSuchIdException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }
}
