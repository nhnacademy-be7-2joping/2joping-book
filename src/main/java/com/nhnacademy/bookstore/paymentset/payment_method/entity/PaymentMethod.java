package com.nhnacademy.bookstore.paymentset.payment_method.entity;

import com.nhnacademy.bookstore.paymentset.payment_method.converter.PaymentMethodTypeConverter;
import com.nhnacademy.bookstore.paymentset.payment_method.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodId;

    @Column(name = "name", nullable = false, unique = true)
    @Convert(converter = PaymentMethodTypeConverter.class)
    private PaymentMethodType paymentMethodType;
}
