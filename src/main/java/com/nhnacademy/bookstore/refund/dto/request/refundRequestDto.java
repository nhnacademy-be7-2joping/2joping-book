package com.nhnacademy.bookstore.refund.dto.request;

import com.nhnacademy.bookstore.refund.enums.RefundPolicyType;

public record refundRequestDto(
        Long orderId,           // 주문 ID
        Long customerId,        // 사용자 ID
        RefundPolicyType reason,    // 반품 사유
        String additionalNote   //
) {
}
