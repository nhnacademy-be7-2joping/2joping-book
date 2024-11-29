package com.nhnacademy.bookstore.user.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record AddressUpdateRequestDto(

        @NotBlank(message = "우편번호는 필수 입력사항입니다.")
        @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자여야 합니다.")
        String postalCode,

        @NotBlank(message = "도로명 주소는 필수 입력사항입니다.")
        @Size(max = 100, message = "도로명 주소는 최대 100자까지 입력 가능합니다.")
        String roadAddress,

        @Size(max = 100, message = "상세 주소는 최대 100자까지 입력 가능합니다.")
        String detailAddress,

        @Size(max = 50, message = "주소 별칭은 최대 50자까지 입력 가능합니다.")
        String addressAlias,

        @NotBlank(message = "수신인 정보는 필수 입력사항입니다.")
        @Size(max = 20, message = "수신인 이름은 최대 20자까지 입력 가능합니다.")
        String receiver

) implements Serializable {}