package com.nhnacademy.bookstore.common.error.exception.shipment;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class CarrierNotFoundException extends NotFoundException {
  public static final String MESSAGE = "해당 배송 업체가 없습니다.";
    public CarrierNotFoundException() {
        super(MESSAGE);
    }
}
