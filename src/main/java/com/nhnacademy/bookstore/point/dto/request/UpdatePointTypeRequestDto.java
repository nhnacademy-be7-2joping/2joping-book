package com.nhnacademy.bookstore.point.dto.request;

import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 포인트 타입 UpdatePointTypeRequest DTO
 *
 * @author : 박채연
 * @date : 2024-11-18
 */

public record UpdatePointTypeRequestDto(
        @NotNull PointTypeEnum type,
        @Positive Integer accVal,
        @NotBlank String name,
        boolean isActive
) {}
