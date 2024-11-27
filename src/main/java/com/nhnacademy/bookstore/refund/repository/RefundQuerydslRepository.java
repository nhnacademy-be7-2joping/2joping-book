package com.nhnacademy.bookstore.refund.repository;

import com.nhnacademy.bookstore.refund.dto.response.RefundResponseDto;

import java.util.List;

public interface RefundQuerydslRepository {
    List<RefundResponseDto> findRefundHistories(Long customerId);
}
