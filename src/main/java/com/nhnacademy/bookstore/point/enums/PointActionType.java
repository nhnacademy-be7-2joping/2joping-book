package com.nhnacademy.bookstore.point.enums;

import lombok.Getter;

@Getter
public enum PointActionType {

    SIGN_UP("회원가입 포인트"),
    REVIEW("리뷰작성 포인트"),
    PURCHASE("구매 적립"),
    USAGE("포인트 사용"),
    REFUND("환불 적립");

    private final String description;

    PointActionType(String description) {
        this.description = description;
    }
}
