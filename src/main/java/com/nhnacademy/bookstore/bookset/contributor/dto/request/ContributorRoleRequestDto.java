package com.nhnacademy.bookstore.bookset.contributor.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 도서 기여자 역할 request dto
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContributorRoleRequestDto {
    @NotBlank(message = "기여자 역할 이름 입력: ")
    private String roleName;
}
