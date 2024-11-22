package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointHistory;

import java.time.LocalDateTime;

public record GetSimplePointHistoriesResponse(

        String name,
        Integer accVal,
        LocalDateTime registerDate
) {
    public static GetSimplePointHistoriesResponse from(PointHistory entity) {
        return new GetSimplePointHistoriesResponse(
                entity.getPointType().getName(),
                entity.getPointType().getAccVal(),
                entity.getRegisterDate()
        );
    }
}
