package com.nhnacademy.bookstore.point.dto.request;

import com.nhnacademy.bookstore.point.entity.PointType;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record CreatePointUseHistoryUseRequest(

        @Nullable
        PointType pointType,

        @Nullable
        Long refundHistoryId,

        @Nullable
        Long orderId,

        Long customerId,
        Integer pointVal,
        LocalDateTime localDateTime
) {
}
