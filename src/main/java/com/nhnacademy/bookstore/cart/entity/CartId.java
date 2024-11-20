package com.nhnacademy.bookstore.cart.entity;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.user.member.entity.Member;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
