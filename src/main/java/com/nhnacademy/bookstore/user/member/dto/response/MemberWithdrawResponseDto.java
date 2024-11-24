package com.nhnacademy.bookstore.user.member.dto.response;

public record MemberWithdrawResponseDto(

        String name,
        String message
    ) {
    public MemberWithdrawResponseDto(String name) {
        this(name, name + "님 탈퇴가 완료되었습니다."); // 고정된 메시지 설정
    }

}
