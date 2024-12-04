package com.nhnacademy.bookstore.tier.enums;

import com.nhnacademy.bookstore.user.tier.enums.Tier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TierTest
 * 이 클래스는 Tier 열거형의 메서드 동작을 검증합니다.
 * 각 등급의 toString() 메서드가 올바른 문자열을 반환하는지 테스트합니다.
 *
 * @since 1.0
 * author Luha
 */
class TierEnumTest {

    /**
     * toString 메서드 테스트
     * Description: Tier 열거형의 각 값에 대해 toString() 메서드가 예상된 문자열을 반환하는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("Tier 열거형 toString 테스트 - 예상 문자열 확인")
    void testToString() {
        // 각 Tier 값에 대해 toString() 호출 결과 검증
        assertEquals("일반", Tier.NORMAL.toString());
        assertEquals("로얄", Tier.ROYAL.toString());
        assertEquals("골드", Tier.GOLD.toString());
        assertEquals("플래티넘", Tier.PLATINUM.toString());
    }
}