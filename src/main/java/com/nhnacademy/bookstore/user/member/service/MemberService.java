package com.nhnacademy.bookstore.user.member.service;

import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberCreateSuccessResponseDto;

public interface MemberService {
    MemberCreateSuccessResponseDto registerNewMember(MemberCreateRequestDto memberDto);
}
