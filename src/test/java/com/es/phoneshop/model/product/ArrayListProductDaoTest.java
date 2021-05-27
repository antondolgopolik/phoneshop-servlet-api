package com.es.phoneshop.model.product;

import com.es.phoneshop.exceptions.NoProductWithSuchIdException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
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
        assertEquals(productDao.findProducts(null), productDao.findProducts(""));
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts("").isEmpty());
    }

    @Test
    public void testFindProductsGeneralFilter() {
        List<Product> products1 = productDao.findProducts("");
        List<Product> products2 = products1.stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
        assertEquals(products1, products2);
    }

    @Test
    public void testSave() throws NoProductWithSuchIdException {
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
    public void testDelete() throws NoProductWithSuchIdException {
        productDao.delete(0L);
        try {
            productDao.getProduct(0L);
            Assert.fail();
        } catch (NoProductWithSuchIdException ignored) {
        }
        productDao.delete(10L);
        try {
            productDao.getProduct(10L);
            Assert.fail();
        } catch (NoProductWithSuchIdException ignored) {
        }
        productDao.delete(12L);
        try {
            productDao.getProduct(12L);
            Assert.fail();
        } catch (NoProductWithSuchIdException ignored) {
        }
    }
}
