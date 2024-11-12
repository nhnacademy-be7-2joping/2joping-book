package com.nhnacademy.bookstore.user.member.entity;

import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.memberStatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Member
 *
 * 회원 정보를 나타내는 엔티티 클래스입니다. 고객 정보를 상속받으며, 회원의 로그인 정보, 포인트, 적립금, 회원 상태 및 등급 등 다양한 정보를 포함합니다.
 * 추가적으로, 회원 가입 시 필요한 정보를 DTO를 통해 엔티티로 변환할 수 있습니다.
 *
 * @author Luha
 * @since 1.0
 */
@Entity
@Table(name = "member")
@PrimaryKeyJoinColumn(name = "customer_id")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member extends Customer {


    @Column(nullable = false, unique = true, length = 20)
    private String loginId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthday;


    @Column(nullable = false)
    private LocalDate joinDate;

    private LocalDate recentLoginDate;

    private boolean isPaycoLogin;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private int accPurchase;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MemberAddress> addresses;

    @ManyToOne
    @JoinColumn(name = "member_status_id", nullable = false)
    @Setter
    private MemberStatus status;

    @ManyToOne
    @JoinColumn(name = "member_tier_id", nullable = false)
    @Setter
    private MemberTier tier;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberCoupon> memberCoupons;


    /**
     * 주어진 DTO를 기반으로 회원의 필드를 초기화합니다.
     *
     * @param requestDto 회원 가입 시 필요한 정보를 담은 DTO 객체
     */
    public void toEntity(MemberCreateRequestDto requestDto) {
        this.initializeCustomerFields(requestDto.getName(), requestDto.getPhone(), requestDto.getEmail());
        this.loginId = requestDto.getLoginId();
        this.password = requestDto.getPassword();
        this.nickname = requestDto.getNickName();
        this.gender = requestDto.getGender();
        this.birthday = requestDto.getBirthday();
        this.joinDate = LocalDate.now();
        this.isPaycoLogin = false;
        this.point = 0;
        this.accPurchase = 0;

    }

    public void addPoint(int amount) {
        this.point += amount;
    }
}
