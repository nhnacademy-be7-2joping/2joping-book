package com.nhnacademy.bookstore.orderset.order.entity;


/**
 * 주문 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */


import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.nhnacademy.bookstore.orderset.order_state.entity.OrderState;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id",nullable = false)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "order_state_id", nullable = false)
    private OrderState orderState;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "coupon_usage_id", nullable = false)
    private Coupon couponUsage;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "receiver", length = 20, nullable = false)
    private String receiver;

    @Column(name = "zipcode", length = 5, nullable = false)
    private String zipcode;

    @Column(name = "road_address", length = 100, nullable = false)
    private String roadAddress;

    @Column(name = "detail_address", length = 100)
    private String detailAddress;

    @Column(name = "point_usage", nullable = false)
    private int pointUsage = 0;

    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;

    @Column(name = "coupon_sale_price", nullable = false)
    private int couponSalePrice = 0;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;
}
