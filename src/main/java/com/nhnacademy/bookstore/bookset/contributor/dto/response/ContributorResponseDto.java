package com.nhnacademy.bookstore.bookset.contributor.dto.response;

import lombok.*;

/**
 * 도서 기여자 response dto
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributorResponseDto {
    private Long contributorId;
    private Long contributorRoleId;
    private String name;
}
