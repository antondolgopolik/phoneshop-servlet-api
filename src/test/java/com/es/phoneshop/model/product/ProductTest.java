package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Currency;

public class ProductTest {

    public static Product[] getMockProducts(int n) {
        Product[] products = new Product[n];
        Currency usd = Currency.getInstance("USD");
        for (int i = 0; i < n; i++) {
            products[i] = new Product(
                    "code " + i, "description " + i,
                    BigDecimal.valueOf(i), usd, i, "url " + i
            );
        }
        return products;
    }

    public static Product getMockProduct() {
        return new Product(
                "code", "description", BigDecimal.ONE,
                Currency.getInstance("USD"), 1, "url"
        );
    }
}
