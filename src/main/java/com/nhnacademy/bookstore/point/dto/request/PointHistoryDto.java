package com.nhnacademy.bookstore.point.dto.request;

import com.nhnacademy.bookstore.point.entity.PointHistory;

import java.time.LocalDateTime;

public record PointHistoryDto(
        Long id,
        Long pointTypeId,
        String pointTypeName,
        Long orderId,
        Long customerId,
        Integer pointVal,
        LocalDateTime registerDate
) {
    public static PointHistoryDto from(PointHistory entity) {
        return new PointHistoryDto(
                entity.getPointTypeHistoryId(),
                entity.getPointType().getPointTypeId(),
                entity.getPointType().getName(),
                entity.getOrderId(),
                entity.getCustomerId(),
                entity.getPointVal(),
                entity.getRegisterDate()
        );
    }
}
