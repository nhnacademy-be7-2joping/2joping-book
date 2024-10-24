package com.nhnacademy.bookstore.bookset.contributor.service.impl;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.exception.NotFoundContributorRoleException;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContributorRoleServiceImpl implements ContributorRoleService {
    private final ContributorRoleRepository contributorRoleRepository;

    // 기여자 역할 생성
    @Override
    @Transactional
    public ContributorRoleResponseDto createContributorRole(ContributorRoleRequestDto dto) {
        ContributorRole contributorRole = new ContributorRole(null, dto.getRoleName());
        ContributorRole savedRole = contributorRoleRepository.save(contributorRole);

        return new ContributorRoleResponseDto(savedRole.getContributorRoleId(), savedRole.getName());
    }
    
    // 기여자 역할 id로 읽기
    @Override
    @Transactional(readOnly = true)
    public ContributorRoleResponseDto getContributorRole(Long contributorRoleId) {
        ContributorRole contributorRole = contributorRoleRepository.findById(contributorRoleId)
                .orElseThrow(() -> new NotFoundContributorRoleException());

        return new ContributorRoleResponseDto(contributorRole.getContributorRoleId(), contributorRole.getName());
    }

    // 기여자 역할 수정
    @Override
    @Transactional
    public ContributorRoleResponseDto updateContributorRole(Long contributorRoleId, ContributorRoleRequestDto dto) {
        ContributorRole contributorRole = contributorRoleRepository.findById(contributorRoleId)
                .orElseThrow(() -> new NotFoundContributorRoleException());

        contributorRole.setName(dto.getRoleName());
        ContributorRole updatedRole = contributorRoleRepository.save(contributorRole);

        return new ContributorRoleResponseDto(updatedRole.getContributorRoleId(), updatedRole.getName());
    }

    // 기여자 역할 삭제
    @Override
    @Transactional
    public void deleteContributorRole(Long contributorRoleId) {
        if (!contributorRoleRepository.existsById(contributorRoleId)) {
            throw new NotFoundContributorRoleException();
        }
        contributorRoleRepository.deleteById(contributorRoleId);
    }
}
