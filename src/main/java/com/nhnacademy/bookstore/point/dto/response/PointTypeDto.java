package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;

public record PointTypeDto(

        Long pointTypeId,
        PointTypeEnum type,
        Integer accVal,
        String name,
        boolean isActive
) {
    public static PointTypeDto from(PointType entity) {
        return new PointTypeDto(
                entity.getPointTypeId(),
                entity.getType(),
                entity.getAccVal(),
                entity.getName(),
                entity.isActive()
        );
    }
}
