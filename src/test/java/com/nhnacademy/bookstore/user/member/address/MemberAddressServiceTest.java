package com.nhnacademy.bookstore.user.member.address;

import com.nhnacademy.bookstore.common.error.exception.user.address.AddressLimitToTenException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import com.nhnacademy.bookstore.user.member.repository.MemberAddressRepositroy;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.impl.MemberAddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 회원 주소 서비스 테스트
 *
 * @author Luha
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class MemberAddressServiceTest {

    @InjectMocks
    private MemberAddressServiceImpl memberAddressService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberAddressRepositroy memberAddressRepositroy;



    /**
     * 테스트: 정상적으로 주소가 추가되는 경우
     * 예상 결과: Member의 기본 주소 목록에 새로운 주소가 추가되고, 응답이 반환된다.
     */
    @Test
    void testAddMemberAddress_Success() {
        // given
        long memberId = 1L;
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L); // ID 필드를 강제로 설정
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "주소 별칭", true, "수신인");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepositroy.countByMemberId(memberId)).thenReturn(5);
        when(memberAddressRepositroy.findByMemberIdAndDefaultAddressTrue(memberId)).thenReturn(null);
        when(memberAddressRepositroy.findAddressesByMemberId(memberId)).thenReturn(Collections.singletonList(new MemberAddressResponseDto(null, null, null, null, null, true, null)));


        // when
        List<MemberAddressResponseDto> result = memberAddressService.addMemberAddress(memberId, requestDto);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(memberRepository).findById(memberId);
        verify(memberAddressRepositroy).countByMemberId(memberId);
        verify(memberAddressRepositroy).findByMemberIdAndDefaultAddressTrue(memberId);
        verify(memberAddressRepositroy).save(any(MemberAddress.class));
    }

    /**
     * 테스트: 회원을 찾을 수 없을 때 예외 발생
     * 예상 결과: MemberNotFoundException이 발생한다.
     */

    @Test
    void testAddMemberAddress_MemberNotFound() {
        long memberId = 1L;
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "주소 별칭", true, "수신인");

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberAddressService.addMemberAddress(memberId, requestDto));
    }

    /**
     * 테스트: 주소 개수가 10개 이상일 때 예외 발생
     * 예상 결과: AddressLimitToTenException이 발생한다.
     */

    @Test
    void testAddMemberAddress_AddressLimitExceeded() {
        long memberId = 1L;
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L); // ID 필드를 강제로 설정
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "주소 별칭", true, "수신인");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepositroy.countByMemberId(memberId)).thenReturn(10);

        assertThrows(AddressLimitToTenException.class, () -> memberAddressService.addMemberAddress(memberId, requestDto));
    }

    /**
     * 테스트: 특정 회원의 주소 목록을 조회하는 경우
     * 예상 결과: 주소 목록이 반환되고, 목록은 비어 있지 않다.
     */

    @Test
    void testGetMemberAddresses_Success() {
        long memberId = 1L;
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L); // ID 필드를 강제로 설정

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepositroy.findAddressesByMemberId(memberId)).thenReturn(Collections.singletonList(new MemberAddressResponseDto(null, null, null, null, null, true, null)));

        List<MemberAddressResponseDto> result = memberAddressService.getMemberAddresses(memberId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(memberRepository).findById(memberId);
        verify(memberAddressRepositroy).findAddressesByMemberId(memberId);
    }

    /**
     * 테스트: 주소 조회 시 회원을 찾을 수 없을 때 예외 발생
     * 예상 결과: MemberNotFoundException이 발생한다.
     */

    @Test
    void testGetMemberAddresses_MemberNotFound() {
        long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberAddressService.getMemberAddresses(memberId));
    }

    /**
     * 테스트: 기본 주소가 설정된 상태에서 새로운 기본 주소가 추가되는 경우
     * 예상 결과: 기존 기본 주소의 isDefaultAddress가 false로 변경된다.
     */

    @Test
    void testAddMemberAddress_WithExistingDefaultAddress() {
        // given
        long memberId = 1L;
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L); // ID 필드를 강제로 설정

        MemberAddressRequestDto requestDto = new MemberAddressRequestDto(
                "12345", "도로명 주소", "상세 주소", "주소 별칭", true, "수신인");

        MemberAddress existingDefaultAddress = new MemberAddress();
        existingDefaultAddress.setDefaultAddress(true);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepositroy.countByMemberId(memberId)).thenReturn(5);
        when(memberAddressRepositroy.findByMemberIdAndDefaultAddressTrue(memberId)).thenReturn(existingDefaultAddress);
        when(memberAddressRepositroy.findAddressesByMemberId(memberId)).thenReturn(Collections.singletonList(new MemberAddressResponseDto(null, null, null, null, null, true, null)));


        // when
        List<MemberAddressResponseDto> result = memberAddressService.addMemberAddress(memberId, requestDto);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(existingDefaultAddress.isDefaultAddress()); // 기존 주소의 기본 설정이 해제되었는지 확인
        verify(memberRepository).findById(memberId);
        verify(memberAddressRepositroy).countByMemberId(memberId);
        verify(memberAddressRepositroy).findByMemberIdAndDefaultAddressTrue(memberId);
        verify(memberAddressRepositroy).save(any(MemberAddress.class));
    }

    /**
     * 테스트: 기본 주소 설정이 없을 때 새 주소를 추가하는 경우
     * 예상 결과: 기본 주소를 설정하는 로직이 호출되지 않는다.
     */

    @Test
    void testAddMemberAddress_WithNoDefaultAddressSetting() {
        // 기본 주소가 false일 때 동작 확인
        long memberId = 1L;
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L); // ID 필드를 강제로 설정

        MemberAddressRequestDto requestDto = new MemberAddressRequestDto(
                "12345", "도로명 주소", "상세 주소", "주소 별칭", false, "수신인");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepositroy.countByMemberId(memberId)).thenReturn(5);

        List<MemberAddressResponseDto> result = memberAddressService.addMemberAddress(memberId, requestDto);

        assertNotNull(result);
        verify(memberAddressRepositroy, never()).findByMemberIdAndDefaultAddressTrue(memberId);
    }
}
