package com.nhnacademy.bookstore.bookset.contributor.dto.response;

import lombok.*;

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
