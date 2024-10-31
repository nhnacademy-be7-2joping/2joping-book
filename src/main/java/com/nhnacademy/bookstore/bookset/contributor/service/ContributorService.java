package com.nhnacademy.bookstore.bookset.contributor.service;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ContributorService {
    ContributorResponseDto createContributor(ContributorRequestDto dto);
    ContributorResponseDto getContributor(Long contributorId);
    ContributorResponseDto updateContributor(Long contributorId, ContributorRequestDto dto);
    void deactivateContributor(Long contributorId);
}
