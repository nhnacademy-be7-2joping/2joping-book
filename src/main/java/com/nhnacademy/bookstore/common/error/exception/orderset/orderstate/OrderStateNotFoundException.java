package com.nhnacademy.bookstore.common.error.exception.orderset.orderstate;

import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;

public class OrderStateNotFoundException extends NotFoundException {

  public static final String MESSAGE = "해당 주문 상태 정보가 없습니다.";

  public OrderStateNotFoundException() {
    super(MESSAGE);
  }
}
