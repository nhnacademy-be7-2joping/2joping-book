package com.nhnacademy.bookstore.user.member.service;

import com.nhnacademy.bookstore.user.member.dto.request.AddressUpdateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.AddressDeleteResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.AddressUpdateResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.MemberAddressResponseDto;

import java.util.List;

/**
 * MemberAddressService
 * 회원 주소 관련 서비스를 정의하는 인터페이스입니다. 이 인터페이스는 회원의 주소 추가 및 조회와 같은 주소 관련 비즈니스 로직을 정의합니다.
 *
 * @since 1.0
 * @author Luha
 */
public interface MemberAddressService {
    List<MemberAddressResponseDto> addMemberAddress(long memberId, MemberAddressRequestDto memberAddressRequestDto);
    List<MemberAddressResponseDto> getMemberAddresses(long memberId);
    AddressDeleteResponseDto deleteMemberAddress(long customerId, long addressId);
    AddressUpdateResponseDto updateMemberAddress(long customerId, long addressId, AddressUpdateRequestDto requestDto);
}
