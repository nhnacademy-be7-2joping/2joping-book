package com.nhnacademy.bookstore.user.tier.service;

import com.nhnacademy.bookstore.user.tier.dto.response.MemberTierResponse;

/**
 * TierService
 * 회원 등급 관련 비즈니스 로직을 정의하는 인터페이스.
 * 회원의 등급 조회 및 관련 데이터를 처리하는 기능을 제공합니다.
 *
 * @author Luha
 * @since 1.0
 */
public interface TierService {

    MemberTierResponse getMemberTier(long customerId);
}
