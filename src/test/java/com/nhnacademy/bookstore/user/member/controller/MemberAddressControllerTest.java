package com.nhnacademy.bookstore.user.member.controller;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.address.AddressLimitToTenException;
import com.nhnacademy.bookstore.common.error.exception.user.address.AddressNotFoundException;
import com.nhnacademy.bookstore.user.member.dto.request.AddressUpdateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.AddressDeleteResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.AddressUpdateResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.service.MemberAddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * MemberAddressControllerTest
 * 이 클래스는 MemberAddressController의 REST API를 테스트합니다.
 * 회원 주소 추가, 조회, 삭제, 업데이트와 관련된 기능을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
@ExtendWith(MockitoExtension.class)
class MemberAddressControllerTest {
    @InjectMocks
    private MemberAddressController memberAddressController;

    @Mock
    private MemberAddressService memberAddressService;

    /**
     * 주소 추가 성공 테스트
     * Description: 주소를 성공적으로 추가했을 때 200 OK 응답과 추가된 주소 정보를 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("주소 추가 - 성공")
    void testAddMemberAddress_Success() {
        // given
        long memberId = 1L;
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "별칭", true, "수신인");

        MemberAddressResponseDto responseDto = new MemberAddressResponseDto(1L, "12345", "도로명 주소", "상세 주소", "별칭", true, "수신인");
        when(memberAddressService.addMemberAddress(memberId, requestDto)).thenReturn(Collections.singletonList(responseDto));

        // when
        ResponseEntity<List<MemberAddressResponseDto>> response = memberAddressController.addMemberAddress(String.valueOf(memberId), requestDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("12345", response.getBody().getFirst().postalCode());
    }

    /**
     * 주소 조회 성공 테스트
     * Description: 특정 회원의 모든 주소를 조회했을 때 200 OK 응답과 주소 리스트를 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 주소 조회 - 성공")
    void testGetAllMemberAddress_Success() {
        // given
        long memberId = 1L;
        MemberAddressResponseDto responseDto = new MemberAddressResponseDto(1L, "12345", "도로명 주소", "상세 주소", "별칭", true, "수신인");
        when(memberAddressService.getMemberAddresses(memberId)).thenReturn(Collections.singletonList(responseDto));

        // when
        ResponseEntity<List<MemberAddressResponseDto>> response = memberAddressController.getAllMemberAddress(String.valueOf(memberId));

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("도로명 주소", response.getBody().getFirst().roadAddress());
    }

    /**
     * 주소 추가 실패 테스트 - 주소 제한 초과
     * Description: 주소 추가 시 최대 개수를 초과했을 때 AddressLimitToTenException 발생을 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("주소 추가 - 주소 제한 초과")
    void testAddMemberAddress_AddressLimitExceededException() {
        // given
        long memberId = 1L;
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "별칭", true, "수신인");
        doThrow(new AddressLimitToTenException("주소는 10개까지 저장할 수 있습니다.", RedirectType.REDIRECT, "signup", null))
                .when(memberAddressService).addMemberAddress(memberId, requestDto);

        // when & then
        assertThrows(AddressLimitToTenException.class, () -> invokeAddMemberAddress(memberId, requestDto));
    }
    private void invokeAddMemberAddress(long memberId, MemberAddressRequestDto requestDto) {
        memberAddressController.addMemberAddress(String.valueOf(memberId), requestDto);
    }

    /**
     * 주소 삭제 성공 테스트
     * Description: 주소를 성공적으로 삭제했을 때 200 OK 응답과 삭제된 주소 정보를 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("주소 삭제 - 성공")
    void testDeleteMemberAddress_Success() {
        // given
        String customerId = "1";
        Long memberAddressId = 10L;
        AddressDeleteResponseDto responseDto = new AddressDeleteResponseDto(memberAddressId, "주소가 성공적으로 삭제되었습니다.");

        when(memberAddressService.deleteMemberAddress(Long.parseLong(customerId), memberAddressId)).thenReturn(responseDto);

        // when
        ResponseEntity<AddressDeleteResponseDto> response = memberAddressController.deleteMemberAddress(customerId, memberAddressId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(memberAddressId, Objects.requireNonNull(response.getBody()).memberAddressId());
        assertEquals("주소가 성공적으로 삭제되었습니다.", response.getBody().message());
    }

    /**
     * 주소 삭제 실패 테스트 - 주소 없음
     * Description: 삭제하려는 주소가 없을 때 AddressNotFoundException 발생을 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("주소 삭제 - 주소 없음")
    void testDeleteMemberAddress_AddressNotFoundException() {
        // given
        String customerId = "1";
        long memberAddressId = 10L;

        doThrow(new AddressNotFoundException("주소를 찾을 수 없습니다.", RedirectType.REDIRECT, "mypage"))
                .when(memberAddressService).deleteMemberAddress(Long.parseLong(customerId), memberAddressId);

        // when & then
        assertThrows(AddressNotFoundException.class, () -> memberAddressController.deleteMemberAddress(customerId, memberAddressId));
    }

    /**
     * 주소 업데이트 성공 테스트
     * Description: 주소를 성공적으로 업데이트했을 때 200 OK 응답과 업데이트된 주소 정보를 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("주소 업데이트 - 성공")
    void testUpdateMemberAddress_Success() {
        // given
        String customerId = "1";
        Long memberAddressId = 10L;
        AddressUpdateRequestDto requestDto = new AddressUpdateRequestDto("12345", "도로명 주소", "상세 주소", "별칭",  "수신인");
        AddressUpdateResponseDto responseDto = new AddressUpdateResponseDto(memberAddressId, "주소가 성공적으로 업데이트되었습니다.");

        when(memberAddressService.updateMemberAddress(Long.parseLong(customerId), memberAddressId, requestDto)).thenReturn(responseDto);

        // when
        ResponseEntity<AddressUpdateResponseDto> response = memberAddressController.updateMemberAddress(customerId, memberAddressId, requestDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(memberAddressId, Objects.requireNonNull(response.getBody()).memberAddressId());
        assertEquals("주소가 성공적으로 업데이트되었습니다.", response.getBody().message());
    }

    /**
     * 주소 업데이트 실패 테스트 - 주소 없음
     * Description: 업데이트하려는 주소가 없을 때 AddressNotFoundException 발생을 확인합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("주소 업데이트 - 주소 없음")
    void testUpdateMemberAddress_AddressNotFoundException() {
        // given
        String customerId = "1";
        long memberAddressId = 10L;
        AddressUpdateRequestDto requestDto = new AddressUpdateRequestDto("12345", "도로명 주소", "상세 주소", "별칭", "수신인");

        doThrow(new AddressNotFoundException("주소를 찾을 수 없습니다.", RedirectType.REDIRECT, "mypage"))
                .when(memberAddressService).updateMemberAddress(Long.parseLong(customerId), memberAddressId, requestDto);

        // when & then
        assertThrows(AddressNotFoundException.class, () -> memberAddressController.updateMemberAddress(customerId, memberAddressId, requestDto));
    }

}
