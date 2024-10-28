package com.nhnacademy.bookstore.user.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MemberAddressResponseDto {

    private Long memberAddressId;      // 주소 ID (DB에서 생성된 ID)

    private String postalCode;        // 우편번호

    private String roadAddress;       // 도로명 주소

    private String detailAddress;     // 상세 주소

    private String addressAlias;      // 주소 별칭

    private boolean isDefaultAddress;  // 기본 주소 여부

    private String receiver;           // 수신인
}
