package com.es.phoneshop.exceptions;

public class NoProductWithSuchIdException extends ProductNotFoundException {

    public NoProductWithSuchIdException(long id) {
        super("The product with ID = " + id + " doesn't exist!");
    }
}
