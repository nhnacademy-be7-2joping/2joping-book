package com.nhnacademy.bookstore.common.error.exception.shipment;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class ShipmentPolicyNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 배송 정책이 없습니다.";
    public ShipmentPolicyNotFoundException() {
        super(MESSAGE);
    }
}
