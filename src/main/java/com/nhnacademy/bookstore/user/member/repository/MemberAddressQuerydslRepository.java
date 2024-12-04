package com.nhnacademy.bookstore.user.member.repository;

import com.nhnacademy.bookstore.user.member.dto.response.address.MemberAddressResponseDto;

import java.util.List;

public interface MemberAddressQuerydslRepository {
    List<MemberAddressResponseDto> findAddressesByMemberId(long memberId);
}
