package com.nhnacademy.bookstore.user.tier.service.impl;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.tier.dto.response.MemberTierResponse;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.service.TierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TierServiceImpl
 * 회원의 등급 정보를 처리하는 서비스 구현체.
 * TierService 인터페이스를 구현하며, 회원 등급 조회 로직을 제공합니다.
 * @author Luha
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class TierServiceImpl implements TierService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public MemberTierResponse getMemberTier(long customerId) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다." + customerId, RedirectType.REDIRECT, "/mypage/mypage"));

        MemberTier tier = member.getTier();
        int usage = member.getAccPurchase();
        int promotion = tier.getMaxPromotion();
        int remaining = promotion - usage;

        if(remaining <= 0) {
            remaining = 0;
        }
        return new MemberTierResponse(usage, tier.getName(), remaining, tier.getAccRate());
    }
}
