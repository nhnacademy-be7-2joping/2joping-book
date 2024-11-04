package com.nhnacademy.bookstore.like.entity;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;
/**
 * 태그 Entity
 *
 * @author : 박채연
 * @date : 2024-10-31
 */
import jakarta.persistence.*;



@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "book_id"})
})

public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Member member; //좋아요를 누른 회원 // 이거 아직 customer가 구현이안돼서 member사용

    @ManyToOne
    @Column(name = "book_id", nullable = false)
    private Book book; // 좋아요 대상인 책


    // 생성자 추가 (선택 사항)
    public Like(Member member, Book book) {
        this.member = member;
        this.book = book;
    }
}

