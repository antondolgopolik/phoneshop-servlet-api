package com.es.phoneshop.model.order;

public enum PaymentMethod {
    CACHE("Cache"), CREDIT_CARD("Credit card");

    private final String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {
        return paymentMethod;
    }
}
