package com.es.phoneshop.exceptions;

public class NoOrderWithSuchIdException extends OrderNotFoundException {

    public NoOrderWithSuchIdException(String id) {
        super("The order with ID = " + id + " doesn't exist!");
    }
}
