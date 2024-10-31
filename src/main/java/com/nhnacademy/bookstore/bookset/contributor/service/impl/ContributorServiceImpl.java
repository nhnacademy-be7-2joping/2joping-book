package com.nhnacademy.bookstore.bookset.contributor.service.impl;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.mapper.ContributorMapper;
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
    private final ContributorMapper contributorMapper;

    // 기여자 생성
    @Override
    @Transactional
    public ContributorResponseDto createContributor(ContributorRequestDto dto) {
        ContributorRole contributorRole = contributorRoleRepository.findById(dto.getContributorRoleId())
                .orElseThrow(ContributorRoleNotFoundException::new);

        Contributor contributor = new Contributor();
        contributor.toEntity(dto, contributorRole);

        Contributor savedContributor = contributorRepository.save(contributor);
        return contributorMapper.toContributorResponseDto(savedContributor);
    }

    // 기여자 id로 읽기
    @Override
    @Transactional(readOnly = true)
    public ContributorResponseDto getContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);

        return contributorMapper.toContributorResponseDto(contributor);
    }

    // 기여자 수정
    @Override
    @Transactional
    public ContributorResponseDto updateContributor(Long contributorId, ContributorRequestDto dto) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);

        ContributorRole contributorRole = contributorRoleRepository.findById(dto.getContributorRoleId())
                .orElseThrow(ContributorRoleNotFoundException::new);

        contributor.toEntity(dto, contributorRole);
        Contributor updatedContributor = contributorRepository.save(contributor);

        return contributorMapper.toContributorResponseDto(updatedContributor);
    }

    // 기여자 비활성화
    @Override
    @Transactional
    public void deactivateContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);
        contributor.deactivate();
        contributorRepository.save(contributor);
    }
}
