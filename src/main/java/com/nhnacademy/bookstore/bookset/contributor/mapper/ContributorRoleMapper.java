package com.nhnacademy.bookstore.bookset.contributor.mapper;

import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContributorRoleMapper {

    // ContributorRole Entity -> ContributorRoleResponseDto 변환
    ContributorRoleResponseDto toContributorRoleResponseDto(ContributorRole contributorRole);
}
