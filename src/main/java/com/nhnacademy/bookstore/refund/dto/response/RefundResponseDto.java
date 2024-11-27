package com.nhnacademy.bookstore.refund.dto.response;

import java.time.LocalDateTime;

public record RefundResponseDto(
        Long refundHistoryId,
        String policy,
        String bookNames,
        LocalDateTime createdAt,
        LocalDateTime approvedAt,
        boolean isApproved
) {
}
