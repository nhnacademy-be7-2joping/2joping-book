package com.nhnacademy.bookstore.bookset.contributor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributorRequestDto {
    @NotBlank(message = "기여자 이름 입력: ")
    private String contributorName;
    @NotNull
    private Long contributorRoleId;
}
