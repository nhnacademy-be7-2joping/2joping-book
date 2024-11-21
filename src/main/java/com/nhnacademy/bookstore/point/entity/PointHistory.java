package com.nhnacademy.bookstore.point.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id")
    private Long pointTypeHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_type_id")
    private PointType pointType;

    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @Column(name = "refund_history_id")
    private Long refundHistoryId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "point_val")
    private Integer pointVal;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @Builder
    public PointHistory(
            PointType pointType,
            Long orderDetailId,
            Long refundHistoryId,
            Long orderId,
            Long customerId,
            Integer pointVal
    ) {
        this.pointType = pointType;
        this.orderDetailId = orderDetailId;
        this.refundHistoryId = refundHistoryId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.pointVal = pointVal;
        this.registerDate = LocalDateTime.now();
    }
}
