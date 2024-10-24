package com.nhnacademy.bookstore.bookset.contributor.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributorRoleRequestDto {
    @NotBlank(message = "기여자 역할 이름 입력: ")
    private String roleName;
}
