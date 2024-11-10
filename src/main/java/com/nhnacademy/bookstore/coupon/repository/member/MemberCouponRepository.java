package com.nhnacademy.bookstore.coupon.repository.member;

import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository  extends JpaRepository<MemberCoupon, Long>, MemberCouponQuerydslRespository{
}
