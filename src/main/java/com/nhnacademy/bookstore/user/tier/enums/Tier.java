package com.nhnacademy.bookstore.user.tier.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Tier
 * 회원 등급을 정의하는 열거형.
 * 각 등급은 회원의 누적 구매 금액에 따라 부여됩니다.
 * 등급별로 혜택과 적립률이 다릅니다.
 *
 * @author Luha
 * @since 1.0
 */
@Getter
public enum Tier {
    NORMAL("일반"),
    ROYAL("로얄"),
    GOLD("골드"),
    PLATINUM("플래티넘");

    private final String koreanName;

    Tier(String koreanName) {
        this.koreanName = koreanName;
    }

    @JsonValue // JSON 변환 시 이 값을 사용
    public String getKoreanName() {
        return koreanName;
    }
    @Override
    public String toString() {
        return koreanName; // 기본 출력은 한글로
    }
}
