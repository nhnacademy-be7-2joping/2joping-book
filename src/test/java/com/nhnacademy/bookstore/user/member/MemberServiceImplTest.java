package com.nhnacademy.bookstore.user.member;


import com.nhnacademy.bookstore.common.error.exception.user.member.MemberDuplicateException;
import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberStatusNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.tier.MemberTierNotFoundException;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberCreateSuccessResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.impl.MemberServiceImpl;
import com.nhnacademy.bookstore.user.memberStatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.memberStatus.repository.MemberStatusRepository;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.repository.MemberTierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 회원 서비스 테스트
 * 회원 등록 및 중복 확인, 예외 처리를 테스트하는 클래스입니다.
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberStatusRepository statusRepository;

    @Mock
    private MemberTierRepository tierRepository;

    private MemberCreateRequestDto memberCreateRequestDto;

    @BeforeEach
    void setUp() {
        memberCreateRequestDto = new MemberCreateRequestDto(
                "testuser", "Test@1234", "이한빈", "010-1234-5678",
                "dlgksqls7218@naver.com", "루하", Gender.MEN, LocalDate.of(1996, 6, 23)
        );
    }

    /**
     * 테스트: 정상적으로 회원이 등록되는 경우
     * 예상 결과: 닉네임이 "루하"로 설정된 응답이 반환된다.
     */
    @Test
    void testRegisterNewMember_Success() {
        // given
        MemberStatus defaultStatus = new MemberStatus(1L, "가입");
        MemberTier defaultTier = new MemberTier(1L, "일반", true, 1, 10000);
        Member member = new Member();
        ReflectionTestUtils.setField(member, "nickname", "루하"); // ID 필드를 강제로 설정


        when(memberRepository.existsByLoginId("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("dlgksqls7218@naver.com")).thenReturn(false);
        when(memberRepository.existsByPhone("010-1234-5678")).thenReturn(false);
        when(statusRepository.findById(1L)).thenReturn(Optional.of(defaultStatus));
        when(tierRepository.findById(1L)).thenReturn(Optional.of(defaultTier));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // when
        MemberCreateSuccessResponseDto response = memberService.registerNewMember(memberCreateRequestDto);

        // then
        assertEquals("루하", response.getNickname());
    }

    /**
     * 테스트: 이미 존재하는 로그인 ID로 등록 시도
     * 예상 결과: MemberDuplicateException 예외가 발생한다.
     */
    @Test
    void testRegisterNewMember_DuplicateLoginId() {
        // given
        when(memberRepository.existsByLoginId("testuser")).thenReturn(true);

        // when & then
        assertThrows(MemberDuplicateException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto));
    }

    /**
     * 테스트: 이미 존재하는 이메일로 등록 시도
     * 예상 결과: MemberDuplicateException 예외가 발생한다.
     */
    @Test
    void testRegisterNewMember_DuplicateEmail() {
        // given
        when(memberRepository.existsByLoginId("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("dlgksqls7218@naver.com")).thenReturn(true);

        // when & then
        assertThrows(MemberDuplicateException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto));
    }

    /**
     * 테스트: 이미 존재하는 전화번호로 등록 시도
     * 예상 결과: MemberDuplicateException 예외가 발생한다.
     */
    @Test
    void testRegisterNewMember_DuplicatePhone() {
        // given
        when(memberRepository.existsByLoginId("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("dlgksqls7218@naver.com")).thenReturn(false);
        when(memberRepository.existsByPhone("010-1234-5678")).thenReturn(true);

        // when & then
        assertThrows(MemberDuplicateException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto));
    }

    /**
     * 테스트: 상태를 찾을 수 없는 경우
     * 예상 결과: MemberStatusNotFoundException 예외가 발생한다.
     */
    @Test
    void testRegisterNewMember_StatusNotFound() {
        // given
        when(memberRepository.existsByLoginId("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("dlgksqls7218@naver.com")).thenReturn(false);
        when(memberRepository.existsByPhone("010-1234-5678")).thenReturn(false);
        when(statusRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberStatusNotFoundException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto));
    }

    /**
     * 테스트: 등급을 찾을 수 없는 경우
     * 예상 결과: MemberTierNotFoundException 예외가 발생한다.
     */
    @Test
    void testRegisterNewMember_TierNotFound() {
        // given
        MemberStatus defaultStatus = new MemberStatus(1L, "가입");

        when(memberRepository.existsByLoginId("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("dlgksqls7218@naver.com")).thenReturn(false);
        when(memberRepository.existsByPhone("010-1234-5678")).thenReturn(false);
        when(statusRepository.findById(1L)).thenReturn(Optional.of(defaultStatus));
        when(tierRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberTierNotFoundException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto));
    }
}