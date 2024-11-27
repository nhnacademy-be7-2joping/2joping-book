package com.nhnacademy.bookstore.refund.service.impl;

import com.nhnacademy.bookstore.refund.dto.response.RefundResponseDto;
import com.nhnacademy.bookstore.refund.repository.RefundRepository;
import com.nhnacademy.bookstore.refund.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;

    @Override
    public List<RefundResponseDto> getRefunds(Long customerId) {

        return refundRepository.findRefundHistories(customerId);
    }
}
