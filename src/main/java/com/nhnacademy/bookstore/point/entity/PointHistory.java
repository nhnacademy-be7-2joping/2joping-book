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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_type_id")
    private PointType pointType;

    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @Column(name = "refund_history_id")
    private Long refundHistoryId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "point_val", nullable = false)
    private Integer pointVal;

    @Column(name = "register_date", nullable = false)
    private LocalDateTime registerDate;

    @PrePersist
    public void prePersist() {
        this.registerDate = LocalDateTime.now();
    }
}
