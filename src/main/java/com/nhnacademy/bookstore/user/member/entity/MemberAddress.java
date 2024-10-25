package com.nhnacademy.bookstore.user.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member_address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberAddress {

    @Id
    @Column(name = "member_address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String roadAddress;

    @Column(length = 100)
    private String detailAddress;

    @Column(length = 50)
    private String addressAlias;

    @Column(nullable = false)
    private boolean isDefaultAddress;

    @Column(nullable = false, length = 20)
    private String receiver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @Setter
    private Member member;
}
