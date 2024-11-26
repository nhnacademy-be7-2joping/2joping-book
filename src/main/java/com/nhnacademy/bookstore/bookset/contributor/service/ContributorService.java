package com.nhnacademy.bookstore.bookset.contributor.service;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorNameRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContributorService {
    ContributorResponseDto createContributor(ContributorRequestDto dto);
    ContributorResponseDto getContributor(Long contributorId);
    ContributorResponseDto updateContributor(Long contributorId, ContributorRequestDto dto);
    void deactivateContributor(Long contributorId);
    void activateContributor(Long contributorId);
    List<ContributorNameRoleResponseDto> getActiveContributorsWithRoles();
    Page<ContributorIsActiveResponseDto> getAllContributors(Pageable pageable);
}
