package com.nhnacademy.bookstore.bookset.contributor.service.impl;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.mapper.ContributorMapper;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 도서 기여자 역할 Service
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Service
@RequiredArgsConstructor
public class ContributorRoleServiceImpl implements ContributorRoleService {
    private final ContributorRoleRepository contributorRoleRepository;
    private final ContributorMapper contributorMapper;

    // 기여자 역할 생성
    @Override
    @Transactional
    public ContributorRoleResponseDto createContributorRole(ContributorRoleRequestDto dto) {
        ContributorRole contributorRole = new ContributorRole();
        contributorRole.toEntity(dto);

        ContributorRole savedRole = contributorRoleRepository.save(contributorRole);
        return contributorMapper.toContributorRoleResponseDto(savedRole);
    }

    // 기여자 역할 id로 읽기
    @Override
    @Transactional(readOnly = true)
    public ContributorRoleResponseDto getContributorRole(Long contributorRoleId) {
        ContributorRole contributorRole = contributorRoleRepository.findById(contributorRoleId)
                .orElseThrow(ContributorRoleNotFoundException::new);

        return contributorMapper.toContributorRoleResponseDto(contributorRole);
    }

    // 기여자 역할 수정
    @Override
    @Transactional
    public ContributorRoleResponseDto updateContributorRole(Long contributorRoleId, ContributorRoleRequestDto dto) {
        ContributorRole contributorRole = contributorRoleRepository.findById(contributorRoleId)
                .orElseThrow(ContributorRoleNotFoundException::new);

        contributorRole.toEntity(dto);
        ContributorRole updatedRole = contributorRoleRepository.save(contributorRole);

        return contributorMapper.toContributorRoleResponseDto(updatedRole);
    }

    // 기여자 역할 삭제
    @Override
    @Transactional
    public void deleteContributorRole(Long contributorRoleId) {
        if (!contributorRoleRepository.existsById(contributorRoleId)) {
            throw new ContributorRoleNotFoundException();
        }
        contributorRoleRepository.deleteById(contributorRoleId);
    }
}
