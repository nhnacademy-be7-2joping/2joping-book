package com.nhnacademy.bookstore.user.member.service;

import com.nhnacademy.bookstore.common.error.exception.user.address.AddressLimitToTenException;
import com.nhnacademy.bookstore.common.error.exception.user.address.AddressNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.user.member.dto.request.AddressUpdateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.AddressDeleteResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.AddressUpdateResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import com.nhnacademy.bookstore.user.member.repository.MemberAddressRepository;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.impl.MemberAddressServiceImpl;
import org.junit.jupiter.api.DisplayName;
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
 * MemberAddressServiceTest
 * 이 클래스는 MemberAddressServiceImpl의 동작을 테스트합니다.
 * 회원 주소 추가, 삭제, 업데이트, 조회 기능의 동작을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
@ExtendWith(MockitoExtension.class)
class MemberAddressServiceTest {

    @InjectMocks
    private MemberAddressServiceImpl memberAddressService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberAddressRepository memberAddressRepository;

    /**
     * 테스트: 정상적으로 주소가 추가되는 경우
     * 예상 결과: Member의 기본 주소 목록에 새로운 주소가 추가되고, 응답이 반환된다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 추가 - 성공")
    void testAddMemberAddress_Success() {
        // given
        long memberId = 1L;
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L); // ID 필드를 강제로 설정
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "주소 별칭", true, "수신인");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepository.countByMemberIdAndAvailableTrue(memberId)).thenReturn(5);
        when(memberAddressRepository.findByMemberIdAndDefaultAddressTrue(memberId)).thenReturn(null);
        when(memberAddressRepository.findAddressesByMemberId(memberId)).thenReturn(Collections.singletonList(new MemberAddressResponseDto(null, null, null, null, null, true, null)));

        // when
        List<MemberAddressResponseDto> result = memberAddressService.addMemberAddress(memberId, requestDto);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(memberRepository).findById(memberId);
        verify(memberAddressRepository).countByMemberIdAndAvailableTrue(memberId);
        verify(memberAddressRepository).findByMemberIdAndDefaultAddressTrue(memberId);
        verify(memberAddressRepository).save(any(MemberAddress.class));
    }

    /**
     * 테스트: 회원을 찾을 수 없을 때 예외 발생
     * 예상 결과: MemberNotFoundException이 발생한다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 추가 - 회원 없음")
    void testAddMemberAddress_MemberNotFound() {
        long memberId = 1L;
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "주소 별칭", true, "수신인");

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberAddressService.addMemberAddress(memberId, requestDto));
    }

    /**
     * 테스트: 주소 개수가 10개 이상일 때 예외 발생
     * 예상 결과: AddressLimitToTenException이 발생한다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 추가 - 주소 개수 초과")
    void testAddMemberAddress_AddressLimitExceeded() {
        long memberId = 1L;
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L); // ID 필드를 강제로 설정
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "주소 별칭", true, "수신인");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepository.countByMemberIdAndAvailableTrue(memberId)).thenReturn(10);

        assertThrows(AddressLimitToTenException.class, () -> memberAddressService.addMemberAddress(memberId, requestDto));
    }

    /**
     * 테스트: 특정 회원의 주소 목록을 조회하는 경우
     * 예상 결과: 주소 목록이 반환되고, 목록은 비어 있지 않다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 조회 - 성공")
    void testGetMemberAddresses_Success() {
        long memberId = 1L;
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L); // ID 필드를 강제로 설정

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepository.findAddressesByMemberId(memberId)).thenReturn(Collections.singletonList(new MemberAddressResponseDto(null, null, null, null, null, true, null)));

        List<MemberAddressResponseDto> result = memberAddressService.getMemberAddresses(memberId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(memberRepository).findById(memberId);
        verify(memberAddressRepository).findAddressesByMemberId(memberId);
    }

    /**
     * 테스트: 주소 조회 시 회원을 찾을 수 없을 때 예외 발생
     * 예상 결과: MemberNotFoundException이 발생한다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 조회 실패 - 회원 없음")
    void testGetMemberAddresses_MemberNotFound() {
        long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberAddressService.getMemberAddresses(memberId));
    }

    /**
     * 테스트: 기본 주소가 설정된 상태에서 새로운 기본 주소가 추가되는 경우
     * 예상 결과: 기존 기본 주소의 isDefaultAddress가 false로 변경된다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 추가 - 기존 기본 주소 업데이트")
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
        when(memberAddressRepository.countByMemberIdAndAvailableTrue(memberId)).thenReturn(5);
        when(memberAddressRepository.findByMemberIdAndDefaultAddressTrue(memberId)).thenReturn(existingDefaultAddress);
        when(memberAddressRepository.findAddressesByMemberId(memberId)).thenReturn(Collections.singletonList(new MemberAddressResponseDto(null, null, null, null, null, true, null)));

        // when
        List<MemberAddressResponseDto> result = memberAddressService.addMemberAddress(memberId, requestDto);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(existingDefaultAddress.isDefaultAddress()); // 기존 주소의 기본 설정이 해제되었는지 확인
        verify(memberRepository).findById(memberId);
        verify(memberAddressRepository).countByMemberIdAndAvailableTrue(memberId);
        verify(memberAddressRepository).findByMemberIdAndDefaultAddressTrue(memberId);
        verify(memberAddressRepository).save(any(MemberAddress.class));
    }

    /**
     * 테스트: 기본 주소 설정이 없을 때 새 주소를 추가하는 경우
     * 예상 결과: 기본 주소를 설정하는 로직이 호출되지 않는다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 추가 - 기본 주소 설정 없음")
    void testAddMemberAddress_WithNoDefaultAddressSetting() {
        // 기본 주소가 false일 때 동작 확인
        long memberId = 1L;
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L); // ID 필드를 강제로 설정

        MemberAddressRequestDto requestDto = new MemberAddressRequestDto(
                "12345", "도로명 주소", "상세 주소", "주소 별칭", false, "수신인");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberAddressRepository.countByMemberIdAndAvailableTrue(memberId)).thenReturn(5);

        List<MemberAddressResponseDto> result = memberAddressService.addMemberAddress(memberId, requestDto);

        assertNotNull(result);
        verify(memberAddressRepository, never()).findByMemberIdAndDefaultAddressTrue(memberId);
    }

    /**
     * 테스트: 주소 삭제 성공 시
     * 예상 결과: 주소의 available이 false로 설정되고 성공 메시지가 반환된다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 삭제 - 성공")
    void testDeleteMemberAddress_Success() {
        long customerId = 1L;
        long addressId = 10L;

        MemberAddress memberAddress = new MemberAddress();
        ReflectionTestUtils.setField(memberAddress, "id", addressId);
        memberAddress.setAvailable(true);

        when(memberAddressRepository.findByMemberIdAndIdAndAvailableTrue(customerId, addressId)).thenReturn(Optional.of(memberAddress));

        AddressDeleteResponseDto response = memberAddressService.deleteMemberAddress(customerId, addressId);

        assertNotNull(response);
        assertEquals(addressId, response.memberAddressId());
        assertEquals("주소가 성공적으로 삭제되었습니다.", response.message());
        assertFalse(memberAddress.isAvailable()); // 삭제 후 available이 false로 변경되었는지 확인
        verify(memberAddressRepository).findByMemberIdAndIdAndAvailableTrue(customerId, addressId);
    }

    /**
     * 테스트: 삭제 시 주소를 찾을 수 없을 때
     * 예상 결과: AddressNotFoundException이 발생한다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 삭제 실패 - 주소 없음")
    void testDeleteMemberAddress_AddressNotFound() {
        long customerId = 1L;
        long addressId = 10L;

        when(memberAddressRepository.findByMemberIdAndIdAndAvailableTrue(customerId, addressId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> memberAddressService.deleteMemberAddress(customerId, addressId));
        verify(memberAddressRepository).findByMemberIdAndIdAndAvailableTrue(customerId, addressId);
    }

    /**
     * 테스트: 주소 업데이트 성공 시
     * 예상 결과: 주소 정보가 수정되고 성공 메시지가 반환된다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 업데이트 - 성공")
    void testUpdateMemberAddress_Success() {
        long customerId = 1L;
        long addressId = 10L;

        MemberAddress memberAddress = new MemberAddress();
        ReflectionTestUtils.setField(memberAddress, "id", addressId);
        memberAddress.setAvailable(true);

        AddressUpdateRequestDto requestDto = new AddressUpdateRequestDto("54321", "새 도로명 주소", "새 상세 주소", "새 별칭", "새 수신인");

        when(memberAddressRepository.findByMemberIdAndIdAndAvailableTrue(customerId, addressId)).thenReturn(Optional.of(memberAddress));

        AddressUpdateResponseDto response = memberAddressService.updateMemberAddress(customerId, addressId, requestDto);

        assertNotNull(response);
        assertEquals(addressId, response.memberAddressId());
        assertEquals("주소가 성공적으로 수정되었습니다.", response.message());
        assertEquals("54321", memberAddress.getPostalCode());
        assertEquals("새 도로명 주소", memberAddress.getRoadAddress());
        assertEquals("새 상세 주소", memberAddress.getDetailAddress());
        assertEquals("새 별칭", memberAddress.getAddressAlias());
        assertFalse(memberAddress.isDefaultAddress());
        assertEquals("새 수신인", memberAddress.getReceiver());
        verify(memberAddressRepository).findByMemberIdAndIdAndAvailableTrue(customerId, addressId);
    }

    /**
     * 테스트: 업데이트 시 주소를 찾을 수 없을 때
     * 예상 결과: AddressNotFoundException이 발생한다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 업데이트 실패 - 주소 없음")
    void testUpdateMemberAddress_AddressNotFound() {
        long customerId = 1L;
        long addressId = 10L;

        AddressUpdateRequestDto requestDto = new AddressUpdateRequestDto("54321", "새 도로명 주소", "새 상세 주소", "새 별칭", "새 수신인");

        when(memberAddressRepository.findByMemberIdAndIdAndAvailableTrue(customerId, addressId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> memberAddressService.updateMemberAddress(customerId, addressId, requestDto));
        verify(memberAddressRepository).findByMemberIdAndIdAndAvailableTrue(customerId, addressId);
    }
}
