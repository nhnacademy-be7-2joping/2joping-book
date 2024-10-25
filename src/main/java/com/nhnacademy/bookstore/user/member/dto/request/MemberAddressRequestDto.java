package com.nhnacademy.bookstore.user.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddressRequestDto {

    @NotBlank(message = "우편번호는 필수 입력사항입니다.")
    @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자여야 합니다.")
    private String postal_code;

    @NotBlank(message = "도로명 주소는 필수 입력사항입니다.")
    @Size(max = 100, message = "도로명 주소는 최대 100자까지 입력 가능합니다.")
    private String road_address;

    @Size(max = 100, message = "상세 주소는 최대 100자까지 입력 가능합니다.")
    private String detail_address;

    @Size(max = 50, message = "주소 별칭은 최대 50자까지 입력 가능합니다.")
    private String address_alias;

    @NotNull(message = "기본 주소 설정 여부는 필수 입력사항입니다.")
    private boolean isDefaultAddress;

    @NotBlank(message = "수신인 정보는 필수 입력사항입니다.")
    @Size(max = 20, message = "수신인 이름은 최대 20자까지 입력 가능합니다.")
    private String receiver;
}