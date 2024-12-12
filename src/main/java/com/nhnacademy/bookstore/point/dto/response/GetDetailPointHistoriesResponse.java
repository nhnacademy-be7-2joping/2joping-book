package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointHistory;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record GetDetailPointHistoriesResponse(

        @Nullable
        String name,

        @Nullable
        Integer accVal,

        PointTypeEnum type,
        boolean isActive,
        LocalDateTime registerDate

) {
    public static GetDetailPointHistoriesResponse from(PointHistory pointHistory) {
        return new GetDetailPointHistoriesResponse(
                pointHistory.getPointType().getName(),
                pointHistory.getPointType().getAccVal(),
                pointHistory.getPointType().getType(),
                pointHistory.getPointType().isActive(),
                pointHistory.getRegisterDate()
        );
    }
}
