package com.nhnacademy.bookstore.user.tier.entity;


import com.nhnacademy.bookstore.user.tier.enums.Tier;
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
@Table(name = "member_tier")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_tier_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Tier name;

    @Column(nullable = false)
    private boolean status; // true: 활성, false: 비활성

    private int accRate;

    private int minPromotion;
    private int maxPromotion;
}