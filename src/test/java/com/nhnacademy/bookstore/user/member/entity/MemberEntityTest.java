package com.nhnacademy.bookstore.user.member.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberEntityTest {

    @Test
    @DisplayName("포인트 추가 테스트")
    void testAddPoint() {
        // Given
        Member member = new Member();
        member.addPoint(10);

        // When
        member.addPoint(20);

        // Then
        assertEquals(30, member.getPoint());
    }
    @Test
    @DisplayName("포인트 사용 테스트")
    void testUsePoint() {
        // Given
        Member member = new Member();
        member.addPoint(50); // 포인트를 먼저 추가

        // When
        member.usePoint(20);

        // Then
        assertEquals(30, member.getPoint());
    }

}
