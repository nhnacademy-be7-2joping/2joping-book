package com.nhnacademy.bookstore.bookset.contributor.mapper;

import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContributorMapper {
    ContributorMapper INSTANCE = Mappers.getMapper(ContributorMapper.class);

    // Contributor Entity -> ContributorResponseDto 변환
    @Mapping(source = "contributor.contributorId", target = "contributorId")
    @Mapping(source = "contributor.contributorRole.contributorRoleId", target = "contributorRoleId")
    @Mapping(source = "contributor.name", target = "name")
    ContributorResponseDto toContributorResponseDto(Contributor contributor);

    // ContributorRole Entity -> ContributorRoleResponseDto 변환
    @Mapping(source = "contributorRole.contributorRoleId", target = "contributorRoleId")
    @Mapping(source = "contributorRole.name", target = "roleName")
    ContributorRoleResponseDto toContributorRoleResponseDto(ContributorRole contributorRole);
}
