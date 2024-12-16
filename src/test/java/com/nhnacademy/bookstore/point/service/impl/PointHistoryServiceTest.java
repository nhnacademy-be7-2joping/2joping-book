package com.nhnacademy.bookstore.point.service.impl;

import com.nhnacademy.bookstore.point.dto.request.CreateOrderPointHistoryRequest;
import com.nhnacademy.bookstore.point.dto.request.CreatePointUseHistoryUseRequest;
import com.nhnacademy.bookstore.point.dto.request.CreateReviewPointHistoryRequest;
import com.nhnacademy.bookstore.point.entity.PointHistory;
import com.nhnacademy.bookstore.point.repository.PointHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PointHistoryServiceTest {

    @InjectMocks
    private PointHistoryService pointHistoryService;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReviewPointHistory() {
        CreateReviewPointHistoryRequest request = new CreateReviewPointHistoryRequest(
                null,
                1L,
                null,
                1L,
                123,
                LocalDateTime.now()
        );

        PointHistory savedPointHistory = PointHistory.builder()
                .pointType(request.pointType())
                .refundHistoryId(request.refundHistoryId())
                .orderId(request.orderId())
                .customerId(request.customerId())
                .pointVal(request.pointVal())
                .registerDate(request.localDateTime())
                .build();

        // NullPointerException을 방지하기 위해 save 메서드에서 반환할 객체에 ID를 설정
        savedPointHistory = PointHistory.builder()
                .pointTypeHistoryId(1L) // ID를 빌더에서 직접 설정
                .pointType(savedPointHistory.getPointType())
                .refundHistoryId(savedPointHistory.getRefundHistoryId())
                .orderId(savedPointHistory.getOrderId())
                .customerId(savedPointHistory.getCustomerId())
                .pointVal(savedPointHistory.getPointVal())
                .registerDate(savedPointHistory.getRegisterDate())
                .build();

        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(savedPointHistory);

        Long result = pointHistoryService.createReviewPointHistory(request);

        assertEquals(1L, result);
    }

    @Test
    void createOrderPointHistory() {
        CreateOrderPointHistoryRequest request = new CreateOrderPointHistoryRequest(
                null,
                null,
                null,
                2L,
                456,
                LocalDateTime.now()
        );

        PointHistory savedPointHistory = PointHistory.builder()
                .pointType(request.pointType())
                .refundHistoryId(request.refundHistoryId())
                .orderId(request.orderId())
                .customerId(request.customerId())
                .pointVal(request.pointVal())
                .registerDate(request.localDateTime())
                .build();

        // NullPointerException을 방지하기 위해 save 메서드에서 반환할 객체에 ID를 설정
        savedPointHistory = PointHistory.builder()
                .pointTypeHistoryId(2L) // ID를 빌더에서 직접 설정
                .pointType(savedPointHistory.getPointType())
                .refundHistoryId(savedPointHistory.getRefundHistoryId())
                .orderId(savedPointHistory.getOrderId())
                .customerId(savedPointHistory.getCustomerId())
                .pointVal(savedPointHistory.getPointVal())
                .registerDate(savedPointHistory.getRegisterDate())
                .build();

        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(savedPointHistory);

        Long result = pointHistoryService.createOrderPointHistory(request);

        assertEquals(2L, result);
    }

    @Test
    void createPointUseHistory() {
        CreatePointUseHistoryUseRequest request = new CreatePointUseHistoryUseRequest(
                null,
                3L,
                null,
                3L,
                789,
                LocalDateTime.now()
        );

        PointHistory savedPointHistory = PointHistory.builder()
                .pointType(request.pointType())
                .refundHistoryId(request.refundHistoryId())
                .orderId(request.orderId())
                .customerId(request.customerId())
                .pointVal(request.pointVal())
                .registerDate(request.localDateTime())
                .build();

        // NullPointerException을 방지하기 위해 save 메서드에서 반환할 객체에 ID를 설정
        savedPointHistory = PointHistory.builder()
                .pointTypeHistoryId(3L) // ID를 빌더에서 직접 설정
                .pointType(savedPointHistory.getPointType())
                .refundHistoryId(savedPointHistory.getRefundHistoryId())
                .orderId(savedPointHistory.getOrderId())
                .customerId(savedPointHistory.getCustomerId())
                .pointVal(savedPointHistory.getPointVal())
                .registerDate(savedPointHistory.getRegisterDate())
                .build();

        when(pointHistoryRepository.save(any(PointHistory.class))).thenReturn(savedPointHistory);

        Long result = pointHistoryService.createPointUseHistory(request);

        assertEquals(3L, result);
    }
}
