package com.nhnacademy.bookstore.like.entity;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.user.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`likes`", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "book_id"})
})

public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Like(Member member, Book book) {
        this.member = member;
        this.book = book;
    }

}

