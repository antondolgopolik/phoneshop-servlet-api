package com.es.phoneshop.model.product;

import com.es.phoneshop.exceptions.NoProductWithSuchIdException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class HashMapProductDaoTest {
    private final ProductDao productDao = HashMapProductDao.getInstance();

    public static void initHashMapProductDao(int n) {
        HashMapProductDao hashMapProductDao = HashMapProductDao.getInstance();
        Product[] products = ProductTest.getMockProducts(n);
        for (Product product : products) {
            hashMapProductDao.save(product);
        }
    }

    @BeforeClass
    public static void classSetup() {
        initHashMapProductDao(100);
    }

    @Test
    public void testGetProduct() throws NoProductWithSuchIdException {
        Product product1 = productDao.getProduct(0L);
        assertEquals((long) product1.getId(), 0L);
        Product product2 = productDao.getProduct(10L);
        assertEquals((long) product2.getId(), 10L);
        Product product3 = productDao.getProduct(12L);
        assertEquals((long) product3.getId(), 12L);
    }

    @Test
    public void testFindProductsNullQuery() {
        assertFalse(productDao.findProducts(null, SortType.DESCRIPTION, OrderType.DESCENDING).isEmpty());
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts("", SortType.PRICE, OrderType.ASCENDING).isEmpty());
    }

    @Test
    public void testFindProductsGeneralFilter() {
        List<Product> products1 = productDao.findProducts("", SortType.DESCRIPTION, OrderType.ASCENDING);
        List<Product> products2 = products1.stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
        assertEquals(products1, products2);
    }

    @Test
    public void testFindProductsDescriptionOrder() {
        List<Product> products1 = productDao.findProducts("", SortType.DESCRIPTION, OrderType.DESCENDING);
        List<Product> products2 = productDao.findProducts("", SortType.DESCRIPTION, OrderType.ASCENDING);
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
        List<Product> products1 = productDao.findProducts("", SortType.PRICE, OrderType.DESCENDING);
        List<Product> products2 = productDao.findProducts("", SortType.PRICE, OrderType.ASCENDING);
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
        assertEquals((long) productDao.getProduct(20L).getId(), 20);
        assertEquals(product1, productDao.getProduct(20L));
        productDao.save(product2);
        assertEquals((long) productDao.getProduct(20L).getId(), 20);
        assertEquals(product2, productDao.getProduct(20L));
    }

    @Test
    public void testDelete() {
        boolean flag = false;
        productDao.delete(0L);
        try {
            productDao.getProduct(0L);
        } catch (NoProductWithSuchIdException ignored) {
            flag = true;
        }
        assertTrue(flag);

        flag = false;
        productDao.delete(10L);
        try {
            productDao.getProduct(10L);
        } catch (NoProductWithSuchIdException ignored) {
            flag = true;
        }
        assertTrue(flag);

        flag = false;
        productDao.delete(12L);
        try {
            productDao.getProduct(12L);
        } catch (NoProductWithSuchIdException ignored) {
            flag = true;
        }
        assertTrue(flag);
    }
}
