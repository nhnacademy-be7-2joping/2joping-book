package com.nhnacademy.bookstore.user.tier.entity;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "tier_name", nullable = false, length = 20)
    private String tierName = "일반"; // 기본값 설정

    @Column(nullable = false)
    private boolean status; // true: 활성, false: 비활성

    @Column(name = "acc_rate", nullable = false)
    private int accRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private int promotion;
}