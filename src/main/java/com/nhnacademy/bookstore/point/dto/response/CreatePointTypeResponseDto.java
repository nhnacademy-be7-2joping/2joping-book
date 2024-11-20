package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;

/**
 * 포인트 타입 CreatePointTypeResponse DTO
 *
 * @author : 박채연
 * @date : 2024-11-18
 */

public record CreatePointTypeResponseDto(

        Long id,
        PointTypeEnum type,
        Integer accVal,
        String name,
        boolean isActive
) {
    public static CreatePointTypeResponseDto from(PointType entity) {
        return new CreatePointTypeResponseDto(
                entity.getId(),
                entity.getType(),
                entity.getAccVal(),
                entity.getName(),
                entity.isActive()
        );
    }
}
