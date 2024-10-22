package com.nhnacademy.bookstore.refund.entity;
/**
 * 반품내역 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refund_history")
@Getter
@Setter
@NoArgsConstructor
public class RefundHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_history_id", nullable = false)
    private Long refundHistoryId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "refund_policy_id", nullable = false)
    private Long refundPolicyId;

    @Column(name = "save_point", nullable = false)
    private Long savePoint = 0L;

    @Column(name = "refund_fee", nullable = false)
    private int refundFee;
}

