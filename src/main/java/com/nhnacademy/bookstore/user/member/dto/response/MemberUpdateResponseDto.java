package com.nhnacademy.bookstore.user.member.dto.response;

import com.nhnacademy.bookstore.user.enums.Gender;

import java.time.LocalDate;

public record MemberUpdateResponseDto(
        String name,
        Gender gender,
        LocalDate birthday,
        String phone,
        String email,
        String nickName
) {
}
