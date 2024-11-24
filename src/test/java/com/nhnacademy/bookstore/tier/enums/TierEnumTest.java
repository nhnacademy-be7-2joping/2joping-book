package com.nhnacademy.bookstore.tier.enums;

import com.nhnacademy.bookstore.user.tier.enums.Tier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TierTest {

    @Test
    void testToString() {
        // 각 Tier 값에 대해 toString() 호출 결과 검증
        assertEquals("일반", Tier.NORMAL.toString());
        assertEquals("로얄", Tier.ROYAL.toString());
        assertEquals("골드", Tier.GOLD.toString());
        assertEquals("플래티넘", Tier.PLATINUM.toString());
    }
}