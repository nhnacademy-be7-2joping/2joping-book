package com.nhnacademy.bookstore.paymentset.payment_history.repository;

import com.nhnacademy.bookstore.paymentset.payment_history.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

}
