package com.nhnacademy.bookstore.bookset.contributor.service.impl;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRepository;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 도서 기여자 Service
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Service
@RequiredArgsConstructor
public class ContributorServiceImpl implements ContributorService {
    private final ContributorRepository contributorRepository;
    private final ContributorRoleRepository contributorRoleRepository;

    // 기여자 생성
    @Override
    @Transactional
    public ContributorResponseDto createContributor(ContributorRequestDto dto) {
        ContributorRole contributorRole = contributorRoleRepository.findById(dto.getContributorRoleId())
                .orElseThrow(ContributorRoleNotFoundException::new);

        Contributor contributor = new Contributor(null, contributorRole, dto.getContributorName(), true);
        Contributor savedContributor = contributorRepository.save(contributor);

        return new ContributorResponseDto(
                savedContributor.getContributorId(),
                savedContributor.getContributorRole().getContributorRoleId(),
                savedContributor.getName()
        );
    }

    // 기여자 id로 읽기
    @Override
    @Transactional(readOnly = true)
    public ContributorResponseDto getContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);

        return new ContributorResponseDto(
                contributor.getContributorId(),
                contributor.getContributorRole().getContributorRoleId(),
                contributor.getName()
        );
    }

    // 기여자 수정
    @Override
    @Transactional
    public ContributorResponseDto updateContributor(Long contributorId, ContributorRequestDto dto) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);

        ContributorRole contributorRole = contributorRoleRepository.findById(dto.getContributorRoleId())
                .orElseThrow(ContributorRoleNotFoundException::new);

        contributor.setName(dto.getContributorName());
        contributor.setContributorRole(contributorRole);

        Contributor updatedContributor = contributorRepository.save(contributor);

        return new ContributorResponseDto(
                updatedContributor.getContributorId(),
                updatedContributor.getContributorRole().getContributorRoleId(),
                updatedContributor.getName()
        );
    }

    // 기여자 비활성화
    @Override
    @Transactional
    public void deactivateContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);
        contributor.setIsActive(false);
        contributorRepository.save(contributor);
    }
}

