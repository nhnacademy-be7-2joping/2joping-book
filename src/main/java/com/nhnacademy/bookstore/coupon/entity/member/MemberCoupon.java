package com.nhnacademy.bookstore.coupon.entity.member;

import com.nhnacademy.bookstore.coupon.entity.Coupon;
import com.nhnacademy.bookstore.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * MemberCoupon 엔티티 클래스
 * 회원이 보유한 쿠폰 정보를 관리하는 클래스입니다. 회원과 쿠폰 간의 다대다 관계를 표현하는 조인 테이블로,
 * 회원이 특정 쿠폰을 언제 받았고 유효 기간은 언제까지인지, 사용 여부 등을 관리합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Entity
@Table(name = "member_coupon")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponUsageId;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member member;

    private LocalDateTime receiveTime;

    private LocalDateTime invalidTime;

    private Boolean isUsed;

    private LocalDateTime usedDate;

    public static MemberCoupon saveMemberCoupon(Coupon coupon, Member member, LocalDateTime receiveTime,
                                                LocalDateTime invalidTime) {
        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.coupon = coupon;
        memberCoupon.member = member;
        memberCoupon.receiveTime = receiveTime;
        memberCoupon.invalidTime = invalidTime;
        memberCoupon.isUsed = false; // 기본값
        memberCoupon.usedDate = null; // 기본값
        return memberCoupon;
    }

    public void updateUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
}