package com.nhnacademy.bookstore.tier.service;



import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.tier.dto.response.MemberTierResponse;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import com.nhnacademy.bookstore.user.tier.service.impl.TierServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


/**
 * TierServiceImplTest
 * 이 클래스는 TierServiceImpl의 비즈니스 로직을 테스트하여 회원 등급 조회 기능의 동작을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
class TierServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TierServiceImpl tierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 회원 등급 조회 성공 테스트
     * 회원 ID로 요청 시 예상된 등급 정보가 반환되는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 등급 조회 - 성공")
    void getMemberTier_Success() {
        // given
        MemberTier mockTier = new MemberTier(1L, Tier.GOLD, true, 10, 10, 20);
        Member mockMember = new Member();
        mockMember.setTier(mockTier);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));

        // when
        MemberTierResponse result = tierService.getMemberTier(1L);

        // then
        assertNotNull(result);
        assertEquals(Tier.GOLD, result.tier());
        assertEquals(20, result.nextTierPrice());
        assertEquals(10, result.accRate());
    }

    /**
     * 회원 등급 조회 실패 테스트 - 회원 ID가 존재하지 않는 경우
     * 존재하지 않는 회원 ID로 요청 시 예외가 발생하는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 등급 조회 - 회원 ID가 존재하지 않음")
    void getMemberTier_NotFound() {
        // given
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> tierService.getMemberTier(999L));

        assertEquals("해당 멤버가 존재하지 않습니다.999", exception.getMessage());
        assertEquals(RedirectType.REDIRECT, exception.getRedirectType());
        assertEquals("/mypage/mypage", exception.getUrl());
    }
    /**
     * 회원 등급 조회 테스트 - remaining 값이 0 이하일 때 처리
     * Description: 등급의 remaining 값이 0 이하일 경우 적절히 처리되는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 등급 조회 - Remaining 값이 0 이하")
    void getMemberTier_RemainingZeroOrBelow() {
        // given
        MemberTier mockTier = new MemberTier(1L, Tier.GOLD, true, 10, 0, 0);
        Member mockMember = new Member();
        mockMember.setTier(mockTier);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));

        // when
        MemberTierResponse result = tierService.getMemberTier(1L);

        // then
        assertNotNull(result);
        assertEquals(0, result.nextTierPrice()); // remaining 값이 0으로 설정되는지 확인
        assertEquals(Tier.GOLD, result.tier());
        assertEquals(10, result.accRate());
    }
}