package com.nhnacademy.bookstore.bookset.book.dto.response;

import lombok.*;

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
