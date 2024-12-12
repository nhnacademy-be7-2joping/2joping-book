package com.nhnacademy.bookstore.paymentset.paymenthistory.entity;

import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.paymentset.paymentmethod.entity.PaymentMethod;
import com.nhnacademy.bookstore.paymentset.status.entity.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentHistoryId;

    @OneToOne
    @JoinColumn(name = "order_id")
    Order order;

    @Column(nullable = false, unique = true)
    String paymentCode;

    @Column(nullable = false)
    LocalDateTime paymentDate;

    @Column(nullable = false)
    Integer amountPaid;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    public void apply(PaymentStatus paymentStatus, PaymentMethod paymentMethod, Order order, String paymentCode,
                      int amountPaid) {
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.order = order;
        this.paymentCode = paymentCode;
        this.paymentDate = LocalDateTime.now();
        this.amountPaid = amountPaid;
    }
}
