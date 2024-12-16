package com.nhnacademy.bookstore.paymentset.paymentmethod.converter;

import com.nhnacademy.bookstore.paymentset.paymentmethod.enums.PaymentMethodType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentMethodTypeConverterTest {

    private final PaymentMethodTypeConverter converter = new PaymentMethodTypeConverter();

    @Test
    @DisplayName("convertToDatabaseColumn: PaymentMethodType -> String 변환 테스트")
    void testConvertToDatabaseColumn() {
        // Given
        PaymentMethodType paymentMethod = PaymentMethodType.CARD;

        // When
        String dbValue = converter.convertToDatabaseColumn(paymentMethod);

        // Then
        assertThat(dbValue).isEqualTo("카드");
    }

    @Test
    @DisplayName("convertToEntityAttribute: String -> PaymentMethodType 변환 테스트")
    void testConvertToEntityAttribute() {
        // Given
        String dbValue = "카드";

        // When
        PaymentMethodType paymentMethod = converter.convertToEntityAttribute(dbValue);

        // Then
        assertThat(paymentMethod).isEqualTo(PaymentMethodType.CARD);
    }

    @Test
    @DisplayName("convertToEntityAttribute: null 입력 시 null 반환 테스트")
    void testConvertToEntityAttributeWithNull() {
        // When
        PaymentMethodType paymentMethod = converter.convertToEntityAttribute(null);

        // Then
        assertThat(paymentMethod).isNull();
    }

    @Test
    @DisplayName("convertToEntityAttribute: 빈 문자열 입력 시 null 반환 테스트")
    void testConvertToEntityAttributeWithEmptyString() {
        // When
        PaymentMethodType paymentMethod = converter.convertToEntityAttribute("");

        // Then
        assertThat(paymentMethod).isNull();
    }

    @Test
    @DisplayName("convertToEntityAttribute: 잘못된 값 입력 시 예외 발생 테스트")
    void testConvertToEntityAttributeWithInvalidValue() {
        // Given
        String invalidValue = "Invalid Value";

        // Then
        assertThatThrownBy(() -> converter.convertToEntityAttribute(invalidValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown value: " + invalidValue);
    }
}
