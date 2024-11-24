package com.nhnacademy.bookstore.orderset.orderstate.dto.response;

import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.orderstate.entity.vo.OrderStateType;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record GetOrderStateResponse(

        Long orderId,
        Long orderStateId,
        Long customerId,

        @Nullable
        Long couponUsageId,

        LocalDateTime orderDate,
        String receiver,
        String postalCode,

        @Nullable
        String detailAddress,

        int couponSalePrice,
        int pointUsage,
        String roadAddress,
        int shippingFee,
        int totalPrice,
        LocalDate desiredDeliveryDate,

        OrderStateType name
) {
    public static GetOrderStateResponse from(Order order) {
        return new GetOrderStateResponse(
                order.getOrderId(),
                order.getOrderState().getOrderStateId(),
                order.getCustomer().getId(),
                order.getCouponUsage().getId(),
                order.getOrderDate(),
                order.getReceiver(),
                order.getPostalCode(),
                order.getDetailAddress(),
                order.getCouponSalePrice(),
                order.getPointUsage(),
                order.getRoadAddress(),
                order.getShippingFee(),
                order.getTotalPrice(),
                order.getDesiredDeliveryDate(),
                order.getOrderState().getName()
        );
    }
}
