package com.nhnacademy.bookstore.user.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;


/**
 * 회원 정보 수정을 위한 요청 데이터를 담는 DTO 클래스입니다.
 * 클라이언트에서 전달받은 데이터를 검증하고, 서비스 계층으로 전달합니다.
 *
 * @author Luha
 * @since 1.0
 */
public record MemberUpdateRequesteDto(


        @NotBlank(message = "전화번호는 필수 입력 사항입니다.")
        @Pattern(regexp = "010-\\d{3,4}-\\d{4}", message = "전화번호는 '010-0000-0000' 형식이어야 합니다.")
        String phone,

        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        @Email(message = "유효한 이메일 주소를 입력해 주세요.")
        String email,

        @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
        @Size(min = 2, max = 20, message = "닉네임은 최소 2자 이상, 최대 20자까지 입력 가능합니다.")
        String nickName

) implements Serializable {
}
