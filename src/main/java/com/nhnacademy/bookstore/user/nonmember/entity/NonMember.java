package com.nhnacademy.bookstore.user.nonmember.entity;

import com.nhnacademy.bookstore.user.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * NonMember
 * 비회원 엔티티 클래스입니다. 이 클래스는 Customer를 상속받아 비회원의 비밀번호 정보를 추가적으로 관리합니다.
 * 비회원은 기본 고객과 동일한 식별자를 가지며, 비회원 전용 필드로 비밀번호가 포함됩니다.
 *
 * @author Luha
 * @since 1.0
 */
@Entity
@Table(name = "non_member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NonMember {
    @Id
    private Long customerId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false, length = 255)
    private String password;

    public void updatePassword(String password) {
        this.password = password;
    }
}
