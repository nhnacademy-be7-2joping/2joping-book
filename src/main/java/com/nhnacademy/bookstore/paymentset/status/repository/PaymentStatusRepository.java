package com.nhnacademy.bookstore.paymentset.status.repository;

import com.nhnacademy.bookstore.paymentset.status.entity.PaymentStatus;
import com.nhnacademy.bookstore.paymentset.status.enums.PaymentStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    Optional<PaymentStatus> findByStatusType(PaymentStatusType statusType);
}
