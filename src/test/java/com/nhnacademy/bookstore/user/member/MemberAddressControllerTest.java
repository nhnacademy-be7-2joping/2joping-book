package com.nhnacademy.bookstore.user.member;

import com.nhnacademy.bookstore.common.error.exception.user.address.AddressLimitToTenException;
import com.nhnacademy.bookstore.user.member.controller.MemberAddressController;
import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.service.MemberAddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberAddressControllerTest {
    @InjectMocks
    private MemberAddressController memberAddressController;

    @Mock
    private MemberAddressService memberAddressService;

    @Test
    public void testAddMemberAddress_Success() {
        // given
        long memberId = 1L;
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "별칭", true, "수신인");

        MemberAddressResponseDto responseDto = new MemberAddressResponseDto(1L, "12345", "도로명 주소", "상세 주소", "별칭", true, "수신인");
        when(memberAddressService.addMemberAddress(memberId, requestDto)).thenReturn(Collections.singletonList(responseDto));

        // when
        ResponseEntity<List<MemberAddressResponseDto>> response = memberAddressController.addMemberAddress(memberId, requestDto);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("12345", response.getBody().get(0).getPostalCode());
    }

    @Test
    public void testGetAllMemberAddress_Success() {
        // given
        long memberId = 1L;
        MemberAddressResponseDto responseDto = new MemberAddressResponseDto(1L, "12345", "도로명 주소", "상세 주소", "별칭", true, "수신인");
        when(memberAddressService.getMemberAddresses(memberId)).thenReturn(Collections.singletonList(responseDto));

        // when
        ResponseEntity<List<MemberAddressResponseDto>> response = memberAddressController.getAllMemberAddress(memberId);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("도로명 주소", response.getBody().get(0).getRoadAddress());
    }

    @Test
    public void testAddMemberAddress_AddressLimitExceededException() {
        // given
        long memberId = 1L;
        MemberAddressRequestDto requestDto = new MemberAddressRequestDto("12345", "도로명 주소", "상세 주소", "별칭", true, "수신인");
        doThrow(new AddressLimitToTenException("주소는 10개까지 저장할 수 있습니다."))
                .when(memberAddressService).addMemberAddress(memberId, requestDto);

        // when & then
        assertThrows(AddressLimitToTenException.class, () -> memberAddressController.addMemberAddress(memberId, requestDto));
    }
}
