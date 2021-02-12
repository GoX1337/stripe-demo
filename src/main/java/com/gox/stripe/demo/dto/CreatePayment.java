package com.gox.stripe.demo.dto;

public class CreatePayment {

    private Object[] items;

    public void setItems(Object[] items) {
        this.items = items;
    }

    public Object[] getItems() {
        return items;
    }
}
