package com.nhnacademy.bookstore.user.tier.dto.response;

import com.nhnacademy.bookstore.user.tier.enums.Tier;

/**
 * MemberTierResponse
 * 회원 등급 정보를 포함하는 응답 객체.
 * 이 객체는 회원의 사용량, 현재 등급, 다음 등급까지 필요한 금액, 적립률 정보를 제공합니다.
 *
 * @author Luha
 * @since 1.0
 */
public record MemberTierResponse(
        int usage,
        Tier tier,
        int nextTierPrice,
        int accRate

) {
}
