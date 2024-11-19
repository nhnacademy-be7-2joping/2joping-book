package com.nhnacademy.bookstore.user.member.dto.request;

import com.nhnacademy.bookstore.user.enums.Gender;

import java.time.LocalDate;

public record UpdateMemberRequest(

        String name,
        Gender gender,
        LocalDate birthday,
        String phone,
        String email,
        String nickname
) {
}
