package com.nhnacademy.bookstore.user.member.entity;

import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "member")
@PrimaryKeyJoinColumn(name = "customer_id")
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Customer {

    @Column(nullable = false, unique = true, length = 20)
    private String id;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthday;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column(nullable = false)
    private LocalDate lastLoginDate;

    private boolean isPaycoLogin;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private int accPurchase;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberAddress> addresses;


}
