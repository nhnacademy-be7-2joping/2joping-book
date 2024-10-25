package com.nhnacademy.bookstore.refund.entity;
/**
 * 반품정책 Entity
 *
 * @author : 이유현
 * @date : 2024-10-22
 */
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refund_policy")
@Getter
@Setter
@NoArgsConstructor
public class RefundPolicy {

    @Id
    @Column(name = "refund_policy_id", nullable = false)
    private Long refundPolicyId;

    @Column(name = "period", nullable = false)
    private int period;

    @Enumerated(EnumType.STRING)
    @Column(name = "policy", nullable = false)
    private PolicyType policy;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "refund_fee", nullable = false)
    private int refundFee;

    public enum PolicyType {
        SIMPLE_CHANGE,  // 단순변심
        DAMAGE          // 파손(파본)
    }
}

