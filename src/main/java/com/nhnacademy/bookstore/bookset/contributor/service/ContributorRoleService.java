package com.nhnacademy.bookstore.bookset.contributor.service;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ContributorRoleService {
    ContributorRoleResponseDto createContributorRole(ContributorRoleRequestDto dto);
    ContributorRoleResponseDto getContributorRole(Long contributorRoleId);
    ContributorRoleResponseDto updateContributorRole(Long contributorRoleId, ContributorRoleRequestDto dto);
    void deleteContributorRole(Long contributorRoleId);
}
