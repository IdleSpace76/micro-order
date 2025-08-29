package ru.micro.order.domain;

import lombok.Getter;

/**
 * @author a.zharov
 */
@Getter
public enum PaymentType {
    CREDIT_CARD("CREDIT_CARD"),
    BANK_TRANSFER("BANK_TRANSFER"),
    PAYPAL("PAYPAL");

    private final String value;

    PaymentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
