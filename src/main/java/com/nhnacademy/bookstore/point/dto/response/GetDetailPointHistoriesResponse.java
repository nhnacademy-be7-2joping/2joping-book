package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointHistory;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;

import java.time.LocalDateTime;

public record GetDetailPointHistoriesResponse(

        String name,
        Integer accVal,
        PointTypeEnum type,
        boolean isActive,
        LocalDateTime registerDate

) {
    public static GetDetailPointHistoriesResponse from(PointHistory entity) {
        return new GetDetailPointHistoriesResponse(
                entity.getPointType().getName(),
                entity.getPointType().getAccVal(),
                entity.getPointType().getType(),
                entity.getPointType().isActive(),
                entity.getRegisterDate()
        );
    }
}
