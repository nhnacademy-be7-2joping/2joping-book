package com.nhnacademy.bookstore.user.nonmember.entity;

import com.nhnacademy.bookstore.user.customer.entity.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * NonMember
 *
 * 비회원 엔티티 클래스입니다. 이 클래스는 Customer를 상속받아 비회원의 비밀번호 정보를 추가적으로 관리합니다.
 * 비회원은 기본 고객과 동일한 식별자를 가지며, 비회원 전용 필드로 비밀번호가 포함됩니다.
 *
 * @author Luha
 * @since 1.0
 */
@Entity
@Table(name = "non_member")
@PrimaryKeyJoinColumn(name = "customer_id")
@NoArgsConstructor
@AllArgsConstructor
public class NonMember extends Customer {

    @Column(nullable = false, length = 255)
    private String password;
}
