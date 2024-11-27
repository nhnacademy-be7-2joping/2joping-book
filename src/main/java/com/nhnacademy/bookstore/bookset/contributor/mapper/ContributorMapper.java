package com.nhnacademy.bookstore.bookset.contributor.mapper;

import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContributorMapper {

    // Contributor Entity -> ContributorResponseDto 변환
    @Mapping(source = "contributorRole.contributorRoleId", target = "contributorRoleId")
    ContributorResponseDto toContributorResponseDto(Contributor contributor);

    // Contributor Entity -> ContributorIsActiveResponseDto 변환
    @Mapping(source = "contributorRole.contributorRoleId", target = "contributorRoleId")
    ContributorIsActiveResponseDto toContributorIsActiveResponseDto(Contributor contributor);
}
