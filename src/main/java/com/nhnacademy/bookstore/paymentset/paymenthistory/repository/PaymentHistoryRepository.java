package com.nhnacademy.bookstore.paymentset.paymenthistory.repository;

import com.nhnacademy.bookstore.paymentset.paymenthistory.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

}
