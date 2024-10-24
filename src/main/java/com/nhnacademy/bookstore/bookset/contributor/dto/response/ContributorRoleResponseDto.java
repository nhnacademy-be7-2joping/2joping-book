package com.nhnacademy.bookstore.bookset.contributor.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributorRoleResponseDto {
    private Long contributorRoleId;
    private String roleName;
}
