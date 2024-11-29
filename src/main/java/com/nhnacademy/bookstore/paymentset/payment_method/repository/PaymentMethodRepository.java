package com.nhnacademy.bookstore.paymentset.payment_method.repository;

import com.nhnacademy.bookstore.paymentset.payment_method.entity.PaymentMethod;
import com.nhnacademy.bookstore.paymentset.payment_method.enums.PaymentMethodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByPaymentMethodType(PaymentMethodType type);
}
