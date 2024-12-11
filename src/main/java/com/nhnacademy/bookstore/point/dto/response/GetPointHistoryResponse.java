package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointHistory;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record GetPointHistoryResponse(

        @Nullable
        String name,

        @Nullable
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
