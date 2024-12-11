package com.nhnacademy.bookstore.orderset.order.entity;


/**
 * 주문 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */


import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderPostRequest;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderRequest;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.orderstate.entity.OrderState;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_code")
    private String orderCode;

    @ManyToOne
    @JoinColumn(name = "order_state_id", nullable = false)
    private OrderState orderState;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_usage_id")
    private MemberCoupon couponUsage;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "desired_delivery_date")
    private LocalDate desiredDeliveryDate;

    @Column(name = "receiver", length = 20, nullable = false)
    private String receiver;

    @Column(name = "postal_code", columnDefinition = "CHAR(5)")
    private String postalCode;

    @Column(name = "road_address", length = 100, nullable = false)
    private String roadAddress;

    @Column(name = "detail_address", length = 100)
    private String detailAddress;

    @Column(name = "point_usage", nullable = false)
    @ColumnDefault("0")
    private int pointUsage = 0;

    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;

    @Column(name = "coupon_sale_price", nullable = false)
    @ColumnDefault("0")
    private int couponSalePrice = 0;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    public void apply(OrderState orderState, MemberCoupon memberCoupon, OrderRequest orderRequest, OrderPostRequest orderPostRequest,
                      Customer customer) {
        LocalDateTime now = LocalDateTime.now();

        this.orderCode = orderPostRequest.orderId();
        this.orderState = orderState;
        this.customer = customer;
        this.couponUsage = memberCoupon;
        this.orderDate = now;
        this.desiredDeliveryDate = LocalDate.parse(orderRequest.deliveryInfo().desiredDate());
        this.receiver = orderRequest.deliveryInfo().receiver();
        this.postalCode = orderRequest.deliveryInfo().postalCode();
        this.roadAddress = orderRequest.deliveryInfo().address();
        this.detailAddress = orderRequest.deliveryInfo().detailAddress();
        this.pointUsage = orderRequest.point();
        this.shippingFee = orderRequest.deliveryCost();
        this.couponSalePrice = orderRequest.couponDiscount();
        this.totalPrice = orderRequest.totalCost();
    }
}
