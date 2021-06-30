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
                    BigDecimal.valueOf(100), usd, 100, "url " + i
            );
        }
        return products;
    }

    public static Product getMockProduct() {
        return new Product(
                "code", "description", BigDecimal.valueOf(100),
                Currency.getInstance("USD"), 100, "url"
        );
    }
}
