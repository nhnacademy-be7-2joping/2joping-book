package com.nhnacademy.bookstore.paymentset.paymentmethod.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum PaymentMethodType {
    CARD("카드", List.of("card", "카드")),
    SIMPLE_PAYMENT("간편결제", List.of("simple_payment", "간편결제"));

    private final String name;
    private final List<String> candidates;
}
