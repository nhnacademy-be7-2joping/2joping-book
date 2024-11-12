package com.nhnacademy.bookstore.orderset.order.dto.response;

import jakarta.validation.constraints.NotNull;

public record OrderShippingFeeRequestDto(
        @NotNull Boolean isMember
) {}
