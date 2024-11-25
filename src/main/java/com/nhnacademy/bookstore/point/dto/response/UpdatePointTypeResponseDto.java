package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;

/**
 * 포인트 타입 UpdatePointTypeResponse DTO
 *
 * @author : 박채연
 * @date : 2024-11-18
 */

public record UpdatePointTypeResponseDto (
        Long id,
        PointTypeEnum type,
        Integer accVal,
        String name,
        boolean isActive
) {
    public static UpdatePointTypeResponseDto from(PointType entity) {
        return new UpdatePointTypeResponseDto(
                entity.getPointTypeId(),
                entity.getType(),
                entity.getAccVal(),
                entity.getName(),
                entity.isActive()
        );
    }
}
