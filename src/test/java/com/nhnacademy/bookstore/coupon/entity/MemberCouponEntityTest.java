package com.nhnacademy.bookstore.coupon.entity;

import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MemberCouponEntityTest
 * MemberCoupon 엔티티의 동작을 검증하는 단위 테스트 클래스입니다.
 * 주요 테스트: updateUsed 메서드의 동작 확인.
 *
 * @since 1.0
 * author Luha
 */
class MemberCouponEntityTest {

    /**
     * 테스트: updateUsed 메서드를 통해 isUsed 값을 true로 업데이트
     * Given: 초기 isUsed 값이 false인 MemberCoupon 객체.
     * When: updateUsed 메서드를 호출하여 isUsed 값을 true로 변경.
     * Then: isUsed 값이 true로 변경되었는지 검증.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("updateUsed 메서드로 isUsed 상태 true로 변경 테스트")
    void testUpdateUsedToTrue() {
        // Given
        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.updateUsed(false); // 초기 상태 false 설정

        // When
        memberCoupon.updateUsed(true); // updateUsed 호출로 상태 변경

        // Then
        assertTrue(memberCoupon.getIsUsed());
    }

}
