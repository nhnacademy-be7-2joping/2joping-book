package com.nhnacademy.bookstore.user.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCreateSuccessResponseDto {
    private String nickname;
    private final String MESSAGE = "님 회원가입을 축하드립니다. 로그인해주세요" ;



}
