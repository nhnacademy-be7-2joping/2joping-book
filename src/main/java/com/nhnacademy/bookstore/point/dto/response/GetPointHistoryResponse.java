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
    public static GetPointHistoryResponse from(PointHistory pointHistory) {
        return new GetPointHistoryResponse(
                pointHistory.getPointType().getName(),
                pointHistory.getPointType().getAccVal(),
                pointHistory.getPointVal(),
                pointHistory.getRegisterDate()
        );
    }
}
