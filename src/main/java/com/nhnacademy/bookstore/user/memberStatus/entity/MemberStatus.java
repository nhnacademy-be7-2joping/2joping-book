package com.nhnacademy.bookstore.user.memberStatus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_status")
@AllArgsConstructor
@NoArgsConstructor
public class MemberStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_status_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String status;  // '가입', '휴면', '탈퇴'

}