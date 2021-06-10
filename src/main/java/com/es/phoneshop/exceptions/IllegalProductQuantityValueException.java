package com.es.phoneshop.exceptions;

public class IllegalProductQuantityValueException extends RuntimeException {

    public IllegalProductQuantityValueException() {
        super("Product quantity should be positive integer number");
    }
}
