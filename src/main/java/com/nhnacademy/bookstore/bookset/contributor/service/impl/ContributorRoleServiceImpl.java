package com.nhnacademy.bookstore.bookset.contributor.service.impl;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.mapper.ContributorRoleMapper;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    private final ContributorRoleMapper contributorRoleMapper;

    /**
     * 도서 기여자 역할을 생성하는 메서드입니다.
     *
     * @param dto 생성할 기여자 역할 정보가 담긴 DTO
     * @return 생성된 기여자 역할의 정보를 포함한 ContributorRoleResponseDto
     */
    @Override
    @Transactional
    public ContributorRoleResponseDto createContributorRole(ContributorRoleRequestDto dto) {
        ContributorRole contributorRole = new ContributorRole();
        contributorRole.toEntity(dto);

        ContributorRole savedRole = contributorRoleRepository.save(contributorRole);
        return contributorRoleMapper.toContributorRoleResponseDto(savedRole);
    }

    /**
     * 특정 ID로 기여자 역할을 조회하는 메서드입니다.
     *
     * @param contributorRoleId 조회할 기여자 역할의 ID
     * @return 조회된 기여자 역할의 정보를 포함한 ContributorRoleResponseDto
     * @throws ContributorRoleNotFoundException 기여자 역할을 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional(readOnly = true)
    public ContributorRoleResponseDto getContributorRole(Long contributorRoleId) {
        ContributorRole contributorRole = contributorRoleRepository.findById(contributorRoleId)
                .orElseThrow(ContributorRoleNotFoundException::new);

        return contributorRoleMapper.toContributorRoleResponseDto(contributorRole);
    }

    /**
     * 특정 ID의 기여자 역할 정보를 수정하는 메서드입니다.
     *
     * @param contributorRoleId 수정할 기여자 역할의 ID
     * @param dto 수정할 기여자 역할 정보가 담긴 DTO
     * @return 수정된 기여자 역할의 정보를 포함한 ContributorRoleResponseDto
     * @throws ContributorRoleNotFoundException 기여자 역할을 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public ContributorRoleResponseDto updateContributorRole(Long contributorRoleId, ContributorRoleRequestDto dto) {
        ContributorRole contributorRole = contributorRoleRepository.findById(contributorRoleId)
                .orElseThrow(ContributorRoleNotFoundException::new);

        contributorRole.toEntity(dto);
        ContributorRole updatedRole = contributorRoleRepository.save(contributorRole);

        return contributorRoleMapper.toContributorRoleResponseDto(updatedRole);
    }

    /**
     * 특정 ID의 기여자 역할을 삭제하는 메서드입니다.
     *
     * @param contributorRoleId 삭제할 기여자 역할의 ID
     * @throws ContributorRoleNotFoundException 기여자 역할을 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public void deleteContributorRole(Long contributorRoleId) {
        if (!contributorRoleRepository.existsById(contributorRoleId)) {
            throw new ContributorRoleNotFoundException();
        }
        contributorRoleRepository.deleteById(contributorRoleId);
    }

    /**
     * 모든 도서 기여자 역할을 조회하는 메서드입니다.
     *
     * @return 모든 기여자 역할의 정보를 포함한 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributorRoleResponseDto> getAllContributorRoles() {
        return contributorRoleRepository.findAll().stream()
                .map(contributorRoleMapper::toContributorRoleResponseDto)
                .collect(Collectors.toList());
    }
}
