package com.nhnacademy.bookstore.coupon.repository.coupon;

import com.nhnacademy.bookstore.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CouponRepository
 * 이 인터페이스는 JpaRepository와 CouponQuerydslRepository를 상속하여 쿠폰 엔티티에 대한
 * 기본적인 CRUD 기능과 Querydsl을 사용한 커스텀 조회 기능을 제공합니다.
 * 쿠폰 이름의 중복 여부를 확인하는 메서드도 포함되어 있습니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponQuerydslRepository {
    boolean existsByName(String name);
    Coupon findByName(String name);
}
