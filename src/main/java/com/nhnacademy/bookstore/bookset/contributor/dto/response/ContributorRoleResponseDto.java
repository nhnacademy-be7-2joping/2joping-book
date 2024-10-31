package com.nhnacademy.bookstore.bookset.contributor.dto.response;

import lombok.*;

/**
 * 도서 기여자 역할 response dto
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContributorRoleResponseDto {
    private Long contributorRoleId;
    private String roleName;
}
