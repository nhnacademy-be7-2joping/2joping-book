package com.nhnacademy.bookstore.user.memberstatus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * MemberStatus
 * 회원 상태 정보를 관리하는 엔티티 클래스입니다. 각 회원의 상태를 나타내는 속성으로, 가입, 휴면, 탈퇴 등의 상태를 가질 수 있습니다.
 * 기본 키는 member_status_id로, 자동 생성됩니다.
 *
 * @author Luha
 * @since 1.0
 */
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