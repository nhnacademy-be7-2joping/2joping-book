package com.nhnacademy.bookstore.user.member.repository;

import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberUpdateResponseDto;

public interface MemberQuerydslRepository {
    MemberUpdateResponseDto updateMemberDetails(MemberUpdateRequesteDto dto, long memberId);
}
