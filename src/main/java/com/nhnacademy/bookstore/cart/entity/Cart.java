package com.nhnacademy.bookstore.cart.entity;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cart")
@Data
public class Cart {

    public Cart() {}

    public Cart(Book book, Member member, int quantity) {

        this.id = new CartId(book.getBookId(), member.getId());

        this.book = book;
        this.member = member;
        this.quantity = quantity;

    }

    @EmbeddedId
    private CartId id;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    private Member member;

    @Column(name = "quantity")
    private int quantity;
}
