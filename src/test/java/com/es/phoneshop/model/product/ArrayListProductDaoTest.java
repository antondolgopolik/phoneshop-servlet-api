package com.es.phoneshop.model.product;

import com.es.phoneshop.exceptions.NoProductWithSuchIdException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSave() {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("code", "des", BigDecimal.ZERO, usd, 0, "url");
        productDao.save(product1);
    }

    @Test
    public void testGetProduct() throws NoProductWithSuchIdException {
        productDao.getProduct(0L);
        productDao.getProduct(10L);
        productDao.getProduct(12L);
    }

    @Test
    public void testSaveAndGet() throws NoProductWithSuchIdException {
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("code", "des", BigDecimal.ZERO, usd, 0, "url");
        productDao.save(product1);
        Product product2 = productDao.getProduct(13L);
        assertEquals(product1, product2);
    }

    @Test
    public void testDelete() throws NoProductWithSuchIdException {
        productDao.delete(0L);
        productDao.delete(10L);
        productDao.delete(12L);
    }
}
