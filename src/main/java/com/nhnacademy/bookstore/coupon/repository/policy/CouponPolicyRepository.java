package com.nhnacademy.bookstore.coupon.repository.policy;

import com.nhnacademy.bookstore.coupon.entity.CouponPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * CouponPolicyRepository
 * 이 인터페이스는 JpaRepository와 CouponPolicyQuerydslRepository를 상속하여 쿠폰 정책 엔티티에 대한
 * 기본적인 CRUD 기능과 Querydsl을 사용한 커스텀 조회 기능을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long>, CouponPolicyQuerydslRepository {

    Optional<CouponPolicy> findByCouponPolicyId(Long id);
    void deleteByCouponPolicyId(Long id);


    // isActive가 true인 쿠폰 정책을 찾는 메서드 추가
    List<CouponPolicy> findByIsActiveTrue();
}
