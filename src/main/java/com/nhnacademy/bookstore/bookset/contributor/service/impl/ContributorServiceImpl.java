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

import java.util.Optional;

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
                .orElseThrow(() -> new ContributorRoleNotFoundException());

        Contributor contributor = new Contributor(null, contributorRole, dto.getContributorName(), true);
        Contributor savedContributor = contributorRepository.save(contributor);

        return new ContributorResponseDto(savedContributor.getContributorId(),
                savedContributor.getContributorRole().getContributorRoleId(),
                savedContributor.getName());
    }

    // 기여자 id로 읽기
    @Override
    @Transactional(readOnly = true)
    public ContributorResponseDto getContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(() -> new ContributorNotFoundException());

        return ContributorResponseDto.builder()
                .contributorId(contributor.getContributorId())
                .contributorRoleId(contributor.getContributorRole().getContributorRoleId())
                .name(contributor.getName())
                .build();
    }

    // 기여자 수정
    @Override
    @Transactional
    public ContributorResponseDto updateContributor(Long contributorId, ContributorRequestDto dto) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(() -> new ContributorNotFoundException());

        Optional<ContributorRole> roleOptional = contributorRoleRepository.findById(dto.getContributorRoleId());

        if (roleOptional.isEmpty()) {
            throw new ContributorRoleNotFoundException();
        }

        contributor.setName(dto.getContributorName());
        contributor.setContributorRole(roleOptional.get());

        Contributor updatedContributor = contributorRepository.save(contributor);

        return ContributorResponseDto.builder()
                .contributorId(updatedContributor.getContributorId())
                .contributorRoleId(updatedContributor.getContributorRole().getContributorRoleId())
                .name(updatedContributor.getName())
                .build();
    }

    // 기여자 비활성화
    @Override
    @Transactional
    public void deactivateContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(() -> new ContributorNotFoundException());
        contributor.setIsActive(false);
        contributorRepository.save(contributor);
    }

}
