package com.nhnacademy.bookstore.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 회원 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

//    @ManyToOne
//    @JoinColumn(name = "member_status_id", nullable = false)
//    private MemberStatus memberStatus;
//
//    @ManyToOne
//    @JoinColumn(name = "member_tier_id", nullable = false)
//    private MemberTier memberTier;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 20, unique = true)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column
    private Boolean gender;

    @Column
    private LocalDate birthday;

    @Column(nullable = false)
    private LocalDateTime joinDate;

    @Column(nullable = false)
    private LocalDateTime recentLoginDate;

    @Column(nullable = false)
    private boolean isPaycoLogin;

    @Column(nullable = false, columnDefinition = "INT default 0")
    private int point;

    @Column(nullable = false, columnDefinition = "INT default 0")
    private int accPurchase;

}

