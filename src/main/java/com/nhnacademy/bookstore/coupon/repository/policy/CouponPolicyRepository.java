package com.nhnacademy.bookstore.coupon.repository.policy;

import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponPolicyRepository  extends JpaRepository<CouponPolicy, Long>, CouponPolicyQuerydslRepository {
}
