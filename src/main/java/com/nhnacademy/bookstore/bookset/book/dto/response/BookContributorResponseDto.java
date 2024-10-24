package com.nhnacademy.bookstore.bookset.book.dto.response;

import lombok.*;

/**
 * 도서 기여자 Response DTO
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookContributorResponseDto {
    private Long contributorId;
    private String contributorName;
    private Long roleId;
    private String roleName;
}
