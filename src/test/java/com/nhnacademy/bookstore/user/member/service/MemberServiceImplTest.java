package com.nhnacademy.bookstore.user.member.service;


import com.nhnacademy.bookstore.common.error.exception.user.member.MemberDuplicateException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberPasswordNotEqualException;
import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberNothingToUpdateException;
import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberStatusNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.tier.MemberTierNotFoundException;
import com.nhnacademy.bookstore.coupon.service.impl.MemberCouponServiceImpl;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberWithdrawRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.*;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.impl.MemberServiceImpl;
import com.nhnacademy.bookstore.user.memberstatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.memberstatus.repository.MemberStatusRepository;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import com.nhnacademy.bookstore.user.tier.repository.MemberTierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 회원 서비스 테스트
 * 회원 등록 및 중복 확인, 예외 처리를 테스트하는 클래스입니다.
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberCouponServiceImpl memberCouponService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberStatusRepository statusRepository;

    @Mock
    private MemberTierRepository tierRepository;

    private MemberCreateRequestDto memberCreateRequestDto;

    Member withdrawMember = new Member();
    private MemberWithdrawRequesteDto withdrawRequestDto;

    @BeforeEach
    void setUp() {
        memberCreateRequestDto = new MemberCreateRequestDto(
                "testuser", "password123", "John Doe", "010-1234-5678",
                "email@example.com", "nickname", Gender.M, LocalDate.of(1990, 1, 1)
        );
        withdrawMember.toEntity(memberCreateRequestDto, "EncodedPassword");
        withdrawRequestDto = new MemberWithdrawRequesteDto("CorrectPassword");

    }

    @Test
    void testRegisterNewMember_Success() {
        // Given
        MemberStatus defaultStatus = new MemberStatus(1L, "가입");
        MemberTier defaultTier = new MemberTier(1L, Tier.GOLD, true, 1, 10000, 100000);
        when(memberRepository.existsByLoginId(memberCreateRequestDto.loginId())).thenReturn(false);
        when(memberRepository.existsByEmail(memberCreateRequestDto.email())).thenReturn(false);
        when(memberRepository.existsByPhone(memberCreateRequestDto.phone())).thenReturn(false);
        when(statusRepository.findById(1L)).thenReturn(Optional.of(defaultStatus));
        when(tierRepository.findById(1L)).thenReturn(Optional.of(defaultTier));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(memberCouponService.saveWelcomeCoupon(any(Member.class))).thenReturn(true);

        // When
        MemberCreateSuccessResponseDto response = memberService.registerNewMember(memberCreateRequestDto);

        // Then
        assertNotNull(response);
        assertEquals(memberCreateRequestDto.nickName(), response.getNickname());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testRegisterNewMember_DuplicateLoginId() {
        // Given
        when(memberRepository.existsByLoginId(memberCreateRequestDto.loginId())).thenReturn(true);

        // When & Then
        assertThrows(MemberDuplicateException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto));
    }

    @Test
    void testRegisterNewMember_StatusNotFound() {
        // Given
        when(memberRepository.existsByLoginId(memberCreateRequestDto.loginId())).thenReturn(false);
        when(statusRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MemberStatusNotFoundException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto));
    }

    @Test
    void testRegisterNewMember_TierNotFound() {
        // Given
        MemberStatus defaultStatus = new MemberStatus(1L, "가입");
        when(memberRepository.existsByLoginId(memberCreateRequestDto.loginId())).thenReturn(false);
        when(statusRepository.findById(1L)).thenReturn(Optional.of(defaultStatus));
        when(tierRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MemberTierNotFoundException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto));
    }

    @Test
    void testUpdateMember_Success() {
        // Given
        long customerId = 1L;
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto(
                "010-9876-5432", "updated-email@example.com", "updatedNick"
        );
        Member member = new Member();
        member.setPhone("010-1234-5678");
        member.setEmail("email@example.com");
        member.setNickname("nickname");

        MemberUpdateResponseDto responseDto = new MemberUpdateResponseDto(
                "John Doe", Gender.M, LocalDate.of(1990, 1, 1),
                "010-9876-5432", "updated-email@example.com", "updatedNick"
        );

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(memberRepository.updateMemberDetails(any(MemberUpdateRequesteDto.class), eq(customerId)))
                .thenReturn(responseDto);

        // When
        MemberUpdateResponseDto actualResponse = memberService.updateMember(customerId, requestDto);

        // Then
        assertNotNull(actualResponse);
        assertEquals("updatedNick", actualResponse.nickName());
        assertEquals("010-9876-5432", actualResponse.phone());
        assertEquals("updated-email@example.com", actualResponse.email());
    }
    @Test
    void testUpdateMember_ChangePhone() {
        // Given
        long customerId = 1L;
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto("010-9876-5432", null, null);

        // Member 객체를 세터를 통해 초기화
        Member member = new Member();
        member.setPhone("010-1234-5678");
        member.setEmail("email@example.com");
        member.setNickname("nickname");

        MemberUpdateResponseDto responseDto = new MemberUpdateResponseDto(
                "John Doe", Gender.M, LocalDate.of(1990, 1, 1),
                "010-9876-5432", "email@example.com", "nickname"
        );

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(memberRepository.existsByPhone("010-9876-5432")).thenReturn(false);
        when(memberRepository.updateMemberDetails(any(MemberUpdateRequesteDto.class), eq(customerId)))
                .thenReturn(responseDto);

        // When
        MemberUpdateResponseDto actualResponse = memberService.updateMember(customerId, requestDto);

        // Then
        assertNotNull(actualResponse);
        assertEquals("010-9876-5432", actualResponse.phone());
        verify(memberRepository).updateMemberDetails(any(MemberUpdateRequesteDto.class), eq(customerId));
    }

    @Test
    void testUpdateMember_ChangeEmail() {
        // Given
        long customerId = 1L;
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto(null, "newemail@example.com", null);

        // Member 객체를 세터를 통해 초기화
        Member member = new Member();
        member.setPhone("010-1234-5678");
        member.setEmail("email@example.com");
        member.setNickname("nickname");

        MemberUpdateResponseDto responseDto = new MemberUpdateResponseDto(
                "John Doe", Gender.M, LocalDate.of(1990, 1, 1),
                "010-1234-5678", "newemail@example.com", "nickname"
        );

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(memberRepository.existsByEmail("newemail@example.com")).thenReturn(false);
        when(memberRepository.updateMemberDetails(any(MemberUpdateRequesteDto.class), eq(customerId)))
                .thenReturn(responseDto);

        // When
        MemberUpdateResponseDto actualResponse = memberService.updateMember(customerId, requestDto);

        // Then
        assertNotNull(actualResponse);
        assertEquals("newemail@example.com", actualResponse.email());
        verify(memberRepository).updateMemberDetails(any(MemberUpdateRequesteDto.class), eq(customerId));
    }

    @Test
    void testUpdateMember_ChangeNickName() {
        // Given
        long customerId = 1L;
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto(null, null, "newNick");

        // Member 객체를 세터를 통해 초기화
        Member member = new Member();
        member.setPhone("010-1234-5678");
        member.setEmail("email@example.com");
        member.setNickname("nickname");

        MemberUpdateResponseDto responseDto = new MemberUpdateResponseDto(
                "John Doe", Gender.M, LocalDate.of(1990, 1, 1),
                "010-1234-5678", "email@example.com", "newNick"
        );

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(memberRepository.updateMemberDetails(any(MemberUpdateRequesteDto.class), eq(customerId)))
                .thenReturn(responseDto);

        // When
        MemberUpdateResponseDto actualResponse = memberService.updateMember(customerId, requestDto);

        // Then
        assertNotNull(actualResponse);
        assertEquals("newNick", actualResponse.nickName());
        verify(memberRepository).updateMemberDetails(any(MemberUpdateRequesteDto.class), eq(customerId));
    }

    @Test
    void testUpdateMember_AllFieldsChange() {
        // Given
        long customerId = 1L;
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto("010-9876-5432", "newemail@example.com", "newNick");

        // Member 객체를 세터를 통해 초기화
        Member member = new Member();
        member.setPhone("010-1234-5678");
        member.setEmail("email@example.com");
        member.setNickname("nickname");

        MemberUpdateResponseDto responseDto = new MemberUpdateResponseDto(
                "John Doe", Gender.M, LocalDate.of(1990, 1, 1),
                "010-9876-5432", "newemail@example.com", "newNick"
        );

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(memberRepository.existsByPhone("010-9876-5432")).thenReturn(false);
        when(memberRepository.existsByEmail("newemail@example.com")).thenReturn(false);
        when(memberRepository.updateMemberDetails(any(MemberUpdateRequesteDto.class), eq(customerId)))
                .thenReturn(responseDto);

        // When
        MemberUpdateResponseDto actualResponse = memberService.updateMember(customerId, requestDto);

        // Then
        assertNotNull(actualResponse);
        assertEquals("010-9876-5432", actualResponse.phone());
        assertEquals("newemail@example.com", actualResponse.email());
        assertEquals("newNick", actualResponse.nickName());
        verify(memberRepository).updateMemberDetails(any(MemberUpdateRequesteDto.class), eq(customerId));
    }
    @Test
    void testUpdateMember_DuplicatePhone() {
        // Given
        long customerId = 1L;
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto("010-9876-5432", null, null);

        Member member = new Member();
        member.setPhone("010-1234-5678");
        member.setEmail("email@example.com");
        member.setNickname("nickname");

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(memberRepository.existsByPhone("010-9876-5432")).thenReturn(true); // 중복된 전화번호 설정

        // When & Then
        MemberDuplicateException exception = assertThrows(
                MemberDuplicateException.class,
                () -> memberService.updateMember(customerId, requestDto)
        );

        assertEquals("이미 존재하는 전화번호입니다.", exception.getMessage());
        verify(memberRepository).existsByPhone("010-9876-5432");
    }
    @Test
    void testUpdateMember_DuplicateEmail() {
        // Given
        long customerId = 1L;
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto(null, "duplicate@example.com", null);

        Member member = new Member();
        member.setPhone("010-1234-5678");
        member.setEmail("email@example.com");
        member.setNickname("nickname");

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(memberRepository.existsByEmail("duplicate@example.com")).thenReturn(true); // 중복된 이메일 설정

        // When & Then
        MemberDuplicateException exception = assertThrows(
                MemberDuplicateException.class,
                () -> memberService.updateMember(customerId, requestDto)
        );

        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
        verify(memberRepository).existsByEmail("duplicate@example.com");
    }
    @Test
    void testGetMemberInfo_Success() {
        // Given
        long customerId = 1L;

        MemberUpdateResponseDto expectedResponse = new MemberUpdateResponseDto(
                "John Doe", Gender.M, LocalDate.of(1990, 1, 1),
                "010-1234-5678", "email@example.com", "nickname"
        );

        when(memberRepository.getMemberInfo(customerId)).thenReturn(expectedResponse);

        // When
        MemberUpdateResponseDto actualResponse = memberService.getMemberInfo(customerId);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.name(), actualResponse.name());
        assertEquals(expectedResponse.gender(), actualResponse.gender());
        assertEquals(expectedResponse.birthday(), actualResponse.birthday());
        assertEquals(expectedResponse.phone(), actualResponse.phone());
        assertEquals(expectedResponse.email(), actualResponse.email());
        assertEquals(expectedResponse.nickName(), actualResponse.nickName());
        verify(memberRepository).getMemberInfo(customerId);
    }

    @Test
    void testUpdateMember_NoChanges() {
        // Given
        long customerId = 1L;
        MemberUpdateRequesteDto requestDto = new MemberUpdateRequesteDto("010-1234-5678", "email@example.com", "nickname");

        Member member = new Member();
        member.setPhone("010-1234-5678");
        member.setEmail("email@example.com");
        member.setNickname("nickname");

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));


        // When
        MemberNothingToUpdateException exception = assertThrows(
                MemberNothingToUpdateException.class,
                () -> memberService.updateMember(customerId, requestDto)
        );

        assertEquals("업데이트할 데이터가 없습니다.", exception.getMessage());
    }
    @Test
    void testRegisterNewMember_EmailAlreadyExists() {
        // Given
        when(memberRepository.existsByLoginId(memberCreateRequestDto.loginId())).thenReturn(false);
        when(memberRepository.existsByEmail(memberCreateRequestDto.email())).thenReturn(true);

        // When & Then
        MemberDuplicateException exception = assertThrows(
                MemberDuplicateException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto)
        );

        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
        verify(memberRepository, never()).existsByPhone(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testRegisterNewMember_EmailDoesNotExist() {
        // Given
        when(memberRepository.existsByLoginId(memberCreateRequestDto.loginId())).thenReturn(false);
        when(memberRepository.existsByEmail(memberCreateRequestDto.email())).thenReturn(false);
        when(memberRepository.existsByPhone(memberCreateRequestDto.phone())).thenReturn(false);

        // 추가 Mock 설정
        when(statusRepository.findById(1L)).thenReturn(Optional.of(new MemberStatus(1L, "가입")));
        when(tierRepository.findById(1L)).thenReturn(Optional.of(new MemberTier(1L, Tier.GOLD, true, 1, 10000, 200000)));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(memberCouponService.saveWelcomeCoupon(any(Member.class))).thenReturn(true);

        // When
        MemberCreateSuccessResponseDto response = memberService.registerNewMember(memberCreateRequestDto);

        // Then
        assertNotNull(response);
        assertEquals("nickname", response.getNickname());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testRegisterNewMember_PhoneAlreadyExists() {
        // Given
        when(memberRepository.existsByLoginId(memberCreateRequestDto.loginId())).thenReturn(false);
        when(memberRepository.existsByEmail(memberCreateRequestDto.email())).thenReturn(false);
        when(memberRepository.existsByPhone(memberCreateRequestDto.phone())).thenReturn(true);

        // When & Then
        MemberDuplicateException exception = assertThrows(
                MemberDuplicateException.class,
                () -> memberService.registerNewMember(memberCreateRequestDto)
        );

        assertEquals("이미 존재하는 전화번호입니다.", exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testWithdrawMember_Success() {
        // Given
        MemberStatus withdrawnStatus = new MemberStatus(3L, "탈퇴");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(withdrawMember));
        when(passwordEncoder.matches("CorrectPassword", "EncodedPassword")).thenReturn(true);
        when(statusRepository.findById(3L)).thenReturn(Optional.of(withdrawnStatus));

        // When
        MemberWithdrawResponseDto response = memberService.withdrawMember(1L, withdrawRequestDto);

        // Then
        assertNotNull(response);
        assertEquals("John Doe", response.name());
        verify(memberRepository).findById(1L);
        verify(passwordEncoder).matches("CorrectPassword", "EncodedPassword");
        verify(statusRepository).findById(3L);
    }

    /**
     * 테스트: 회원이 존재하지 않을 때 예외 발생
     * 예상 결과: MemberNotFoundException 발생
     */
    @Test
    void testWithdrawMember_MemberNotFound() {
        // Given
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        MemberNotFoundException exception = assertThrows(
                MemberNotFoundException.class,
                () -> memberService.withdrawMember(1L, withdrawRequestDto)
        );

        assertEquals("해당 멤버가 존재하지 않습니다.", exception.getMessage());
        verify(memberRepository).findById(1L);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(statusRepository, never()).findById(anyLong());
    }

    /**
     * 테스트: 비밀번호가 일치하지 않을 때 예외 발생
     * 예상 결과: MemberPasswordNotEqualException 발생
     */
    @Test
    void testWithdrawMember_PasswordNotEqual() {
        // Given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(withdrawMember));
        when(passwordEncoder.matches("CorrectPassword", "EncodedPassword")).thenReturn(false);

        // When & Then
        MemberPasswordNotEqualException exception = assertThrows(
                MemberPasswordNotEqualException.class,
                () -> memberService.withdrawMember(1L, withdrawRequestDto)
        );

        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(memberRepository).findById(1L);
        verify(passwordEncoder).matches("CorrectPassword", "EncodedPassword");
        verify(statusRepository, never()).findById(anyLong());
    }

    /**
     * 테스트: 회원 상태를 찾지 못했을 때 예외 발생
     * 예상 결과: MemberStatusNotFoundException 발생
     */
    @Test
    void testWithdrawMember_StatusNotFound() {
        // Given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(withdrawMember));
        when(passwordEncoder.matches("CorrectPassword", "EncodedPassword")).thenReturn(true);
        when(statusRepository.findById(3L)).thenReturn(Optional.empty());

        // When & Then
        MemberStatusNotFoundException exception = assertThrows(
                MemberStatusNotFoundException.class,
                () -> memberService.withdrawMember(1L, withdrawRequestDto)
        );

        assertEquals("회원 상태가 존재하지 않습니다.", exception.getMessage());
        verify(memberRepository).findById(1L);
        verify(passwordEncoder).matches("CorrectPassword", "EncodedPassword");
        verify(statusRepository).findById(3L);
    }

    @Test
    @DisplayName("회원의 포인트 조회")
    void testGetPointsOfMember() {
        MemberPointResponse mockPoint = new MemberPointResponse(100);
        when(memberRepository.findPointById(1L)).thenReturn(Optional.of(mockPoint));

        MemberPointResponse actual = memberService.getPointsOfMember(1L);
        assertNotNull(actual);
        assertEquals(mockPoint, actual);
    }

    @Test
    @DisplayName("전체 회원 조회 - 성공")
    void testGetAllMembers_Success() {

        int page = 0;
        Pageable pageable = PageRequest.of(page, 10); // 페이지 크기 10 설정

        MemberTier tier = new MemberTier(1L, Tier.NORMAL, true, 1, 1, 1);
        Member member1 = new Member("testLoginId1", "Test Member1", "nick1", Gender.M, LocalDate.now(), 10, LocalDate.now(), null, false, 0, 0, null,
                null, null, tier, null);
        Member member2 = new Member("testLoginId2", "Test Member2", "nick2", Gender.M, LocalDate.now(), 10, LocalDate.now(), null, false, 0, 0, null,
                null, null, tier, null);

        List<Member> members = List.of(member1, member2);
        Page<Member> memberPage = new PageImpl<>(members, pageable, members.size());

        when(memberRepository.findAllByOrderByNicknameDesc(pageable)).thenReturn(memberPage);

        List<GetAllMembersResponse> actualResponse = memberService.getAllMembers(page);

        assertNotNull(actualResponse);
        assertEquals(2, actualResponse.size());
    }

    @Test
    @DisplayName("회원 포인트 조회 실패 - 회원 정보 없음")
    void testGetPointsOfMember_NotFound() {
        // Given
        Long customerId = 1L;

        // Mock: memberRepository.findPointById가 Optional.empty()를 반환하도록 설정
        when(memberRepository.findPointById(customerId)).thenReturn(Optional.empty());

        // When & Then
        MemberNotFoundException exception = assertThrows(
                MemberNotFoundException.class,
                () -> memberService.getPointsOfMember(customerId)
        );

        // Exception 메시지 검증
        assertEquals("회원 정보를 찾을 수 없습니다.", exception.getMessage());

        // Repository 호출 검증
        verify(memberRepository).findPointById(customerId);
    }

    /**
     * 테스트: 회원 정보를 업데이트하려고 할 때, 존재하지 않는 회원인 경우
     * 예상 결과: MemberNotFoundException이 발생한다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 업데이트 실패 - 존재하지 않는 회원")
    void testUpdateMember_NotFound() {
        // given
        long customerId = 999L; // 존재하지 않는 ID
        MemberUpdateRequesteDto updateRequest = new MemberUpdateRequesteDto(
                "010-1234-5678",
                "test@example.com",
                "newNick"
        );

        // 필요한 Mock 설정만 유지
        when(memberRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MemberNotFoundException.class, () -> memberService.updateMember(customerId, updateRequest));

        // Verify: findById가 호출되었는지 확인
        verify(memberRepository, times(1)).findById(customerId);
        // updateMemberDetails는 호출되지 않아야 함
        verify(memberRepository, never()).updateMemberDetails(any(MemberUpdateRequesteDto.class), anyLong());
    }
}