package com.es.phoneshop.exceptions;

public class ProductNotEnoughException extends RuntimeException {

    public ProductNotEnoughException(int availableQuantity) {
        super("Product isn't enough, it's " + availableQuantity + " qty. available only");
    }
}
