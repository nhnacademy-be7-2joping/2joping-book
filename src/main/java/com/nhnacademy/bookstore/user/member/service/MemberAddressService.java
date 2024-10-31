package com.nhnacademy.bookstore.user.member.service;

import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;

import java.util.List;

public interface MemberAddressService {
    List<MemberAddressResponseDto> addMemberAddress(long memberId, MemberAddressRequestDto memberAddressRequestDto);
    List<MemberAddressResponseDto> getMemberAddresses(long memberId);
}
