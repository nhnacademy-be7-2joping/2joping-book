package com.nhnacademy.bookstore.cart.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
public class CartId implements Serializable {

    public CartId() {}

    public CartId(Long bookId, Long customerId) {
        this.bookId = bookId;
        this.customerId = customerId;
    }

    private Long bookId;
    private Long customerId;
}
