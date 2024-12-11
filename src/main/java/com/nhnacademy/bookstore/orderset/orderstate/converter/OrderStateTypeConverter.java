package com.nhnacademy.bookstore.orderset.orderstate.converter;

import com.nhnacademy.bookstore.orderset.orderstate.entity.vo.OrderStateType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStateTypeConverter implements AttributeConverter<OrderStateType, String> {
    @Override
    public String convertToDatabaseColumn(OrderStateType orderStateType) {
        return orderStateType.getDescription();
    }

    @Override
    public OrderStateType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        for (OrderStateType type : OrderStateType.values()) {
            if (type.getDescription().equals(dbData)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + dbData);
    }
}
