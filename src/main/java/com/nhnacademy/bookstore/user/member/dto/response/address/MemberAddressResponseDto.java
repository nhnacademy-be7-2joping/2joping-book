package com.nhnacademy.bookstore.user.member.dto.response.address;

import com.fasterxml.jackson.annotation.JsonProperty;


public record MemberAddressResponseDto(
        Long memberAddressId,       // 주소 ID (DB에서 생성된 ID)
        String postalCode,          // 우편번호
        String roadAddress,         // 도로명 주소
        String detailAddress,       // 상세 주소
        String addressAlias,        // 주소 별칭
        @JsonProperty("isDefaultAddress")
        boolean isDefaultAddress,   // 기본 주소 여부
        String receiver             // 수신인
) {
}

