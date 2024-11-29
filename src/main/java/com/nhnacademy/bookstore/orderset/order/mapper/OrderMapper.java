package com.nhnacademy.bookstore.orderset.order.mapper;

import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order.dto.OrderListResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "order.orderState.orderStateId", target = "orderStateId")
    @Mapping(source = "order.orderId", target = "orderId")
    @Mapping(source = "order.customer.name", target = "customerName")
    @Mapping(source = "order.couponUsage.coupon.name", target = "couponName")
    @Mapping(source = "order.receiver", target = "receiver")
    @Mapping(source = "order.postalCode", target = "postalCode")
    @Mapping(source = "order.roadAddress", target = "roadAddress")
    @Mapping(source = "order.detailAddress", target = "detailAddress")
    @Mapping(source = "order.pointUsage", target = "pointUsage")
    @Mapping(source = "order.shippingFee", target = "shippingFee")
    @Mapping(source = "order.couponSalePrice", target = "couponSalePrice")
    @Mapping(source = "order.totalPrice", target = "totalPrice")
    @Mapping(source = "order.desiredDeliveryDate", target = "desiredDeliveryDate")
    OrderListResponseDto toOrderResponseDto(Order order);

    List<OrderListResponseDto> toOrderListResponseDto(List<Order> orderList);
}


