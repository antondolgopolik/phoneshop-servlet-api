package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductTest;

public class CartItemTest {

    public static CartItem[] getMockCartItems(int n) {
        Product[] products = ProductTest.getMockProducts(n);
        CartItem[] cartItems = new CartItem[n];
        for (int i = 0; i < n; i++) {
            cartItems[i] = new CartItem(products[i], 10);
        }
        return cartItems;
    }

    public static CartItem getMockCartItem() {
        return new CartItem(ProductTest.getMockProduct(), 10);
    }
}
