package com.nhnacademy.bookstore.orderset.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record OrderRequest(
        List<@Valid CartItemRequest> cartItemList,

        @NotNull
        DeliveryInfoRequest deliveryInfo,

        @Min(value = 0)
        Integer point,

        @Min(value = -1)
        Long couponId,

        List<@Valid WrapItemRequest> wrapList,

        @Min(value = 0)
        Integer bookCost,

        @Min(value = 0)
        Integer deliveryCost,

        @Min(value = 0)
        Integer wrapCost,

        @Min(value = 0)
        Integer totalCost,

        @Min(value = 0)
        Integer couponDiscount,
        String nonMemberPassword,

        @NotBlank
        String orderCode
) {
    public record CartItemRequest(
            @NotNull
            @Positive
            Long bookId,

            @NotNull
            @Min(value = 1)
            Integer quantity,

            @NotNull
            @Min(value = 0)
            Integer unitPrice
    ) {
    }

    public record DeliveryInfoRequest(
            @NotBlank(message = "받는이를 비워둘 수 없습니다.")
            String receiver,

            @NotBlank
            String desiredDate,

            @NotBlank
            @Pattern(regexp = "\\d{5}")
            String postalCode,

            @NotBlank
            String address,

            String detailAddress,

            @NotBlank
            String name,

            @NotBlank
            @Pattern(regexp = "^(01[0-9])-?[0-9]{3,4}-?[0-9]{4}$")
            String phone,

            @NotBlank(message = "이메일은 필수입니다.")
            @Email
            String email,

            String requirement,

            @NotNull
            @Positive
            Long deliveryPolicyId
    ) {

    }

    public record WrapItemRequest(
            @NotNull
            Long bookId,
            Long wrapId
    ) {
    }
}
