package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointHistory;

import java.time.LocalDateTime;

public record GetPointHistoryResponse(

        String name,
        Integer accVal,
        Integer pointVal,
        LocalDateTime registerDate
) {
    public static GetPointHistoryResponse from(PointHistory entity) {
        return new GetPointHistoryResponse(
                entity.getPointType().getName(),
                entity.getPointType().getAccVal(),
                entity.getPointVal(),
                entity.getRegisterDate()
        );
    }
}
