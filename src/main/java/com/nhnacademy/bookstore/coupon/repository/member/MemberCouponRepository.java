package com.nhnacademy.bookstore.coupon.repository.member;

import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MemberCouponRepository
 * 이 인터페이스는 JpaRepository와 MemberCouponQuerydslRepository를 상속하여 회원 쿠폰 엔티티에 대한
 * 기본적인 CRUD 기능과 Querydsl을 사용한 커스텀 조회 기능을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface MemberCouponRepository  extends JpaRepository<MemberCoupon, Long>, MemberCouponQuerydslRespository{
}
