package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointHistory;

import java.time.LocalDateTime;

public record PointHistoryDto(

        String name,
        Integer accVal,
        Integer pointVal,
        LocalDateTime registerDate
) {
    public static PointHistoryDto from(PointHistory entity) {
        return new PointHistoryDto(
                entity.getPointType().getName(),
                entity.getPointType().getAccVal(),
                entity.getPointVal(),
                entity.getRegisterDate()
        );
    }
}
