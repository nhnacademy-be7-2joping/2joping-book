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
        Long pointTypeId,
        PointTypeEnum type,
        Integer accVal,
        String name,
        boolean isActive
) {
//    public UpdatePointTypeResponseDto toUpdateResponseDto(PointType entity) {
//        System.out.println("Mapping ID: " + entity.getPointTypeId()); // 매핑 확인
//        return new UpdatePointTypeResponseDto(
//                entity.getPointTypeId(),
//                entity.getType(),
//                entity.getAccVal(),
//                entity.getName(),
//                entity.isActive()
//        );
//    }


}
