package com.nhnacademy.bookstore.coupon.repository.coupon;

import com.nhnacademy.bookstore.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponQuerydslRepositroy{
    boolean existsByName(String name);
}
