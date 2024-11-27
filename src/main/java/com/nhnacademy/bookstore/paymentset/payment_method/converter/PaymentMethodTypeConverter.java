package com.nhnacademy.bookstore.paymentset.payment_method.converter;

import com.nhnacademy.bookstore.paymentset.payment_method.enums.PaymentMethodType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodTypeConverter implements AttributeConverter<PaymentMethodType, String> {
    @Override
    public String convertToDatabaseColumn(PaymentMethodType paymentMethod) {
        return paymentMethod.getName();
    }

    @Override
    public PaymentMethodType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        for (PaymentMethodType paymentMethod : PaymentMethodType.values()) {
            if (paymentMethod.getName().equals(dbData)) {
                return paymentMethod;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + dbData);
    }
}
