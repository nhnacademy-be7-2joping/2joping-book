package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;

public record GetPointTypeResponse(

        Long pointTypeId,
        PointTypeEnum type,
        Integer accVal,
        String name,
        boolean isActive
) {
    public static GetPointTypeResponse from(PointType entity) {
        return new GetPointTypeResponse(
                entity.getPointTypeId(),
                entity.getType(),
                entity.getAccVal(),
                entity.getName(),
                entity.isActive()
        );
    }
}
