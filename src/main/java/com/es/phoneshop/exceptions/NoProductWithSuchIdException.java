package com.es.phoneshop.exceptions;

public class NoProductWithSuchIdException extends Exception {

    public NoProductWithSuchIdException(long id) {
        super("The product with ID = " + id + " doesn't exist!");
    }
}
