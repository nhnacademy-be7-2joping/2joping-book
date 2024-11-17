package com.nhnacademy.bookstore.user.member.dto.request;

import com.nhnacademy.bookstore.user.enums.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MemberUpdateRequesteDto(

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        String name,

        @NotNull(message = "성별은 필수 입력 사항입니다.")
        Gender gender,

        @Past(message = "생년월일은 과거 날짜만 가능합니다.")
        LocalDate birthday,

        @NotBlank(message = "전화번호는 필수 입력 사항입니다.")
        @Pattern(regexp = "010-\\d{3,4}-\\d{4}", message = "전화번호는 '010-0000-0000' 형식이어야 합니다.")
        String phone,

        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        @Email(message = "유효한 이메일 주소를 입력해 주세요.")
        String email,

        @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
        @Size(min = 2, max = 20, message = "닉네임은 최소 2자 이상, 최대 20자까지 입력 가능합니다.")
        String nickName

) {
}
