package com.nhnacademy.bookstore.bookset.contributor.service.impl;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorNameRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.mapper.ContributorMapper;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorIsDeactivateException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRepository;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 새로운 기여자를 생성하는 메서드입니다.
     *
     * @param dto 생성할 기여자 정보가 담긴 ContributorRequestDto
     * @return 생성된 기여자의 정보를 담은 ContributorResponseDto
     * @throws ContributorRoleNotFoundException 기여자 역할이 존재하지 않을 경우 발생
     */
    @Override
    @Transactional
    public ContributorResponseDto createContributor(ContributorRequestDto dto) {
        ContributorRole contributorRole = contributorRoleRepository.findById(dto.contributorRoleId())
                .orElseThrow(ContributorRoleNotFoundException::new);

        Contributor contributor = new Contributor(null, contributorRole, dto.name(), true);

        Contributor savedContributor = contributorRepository.save(contributor);
        return contributorMapper.toContributorResponseDto(savedContributor);
    }

    /**
     * 기여자 ID로 기여자 정보를 조회하는 메서드입니다.
     *
     * @param contributorId 조회할 기여자의 ID
     * @return 조회된 기여자의 정보를 담은 ContributorResponseDto
     * @throws ContributorNotFoundException 기여자를 찾을 수 없을 경우 발생
     * @throws ContributorIsDeactivateException 기여자가 비활성화 상태인 경우 발생
     */
    @Override
    @Transactional(readOnly = true)
    public ContributorResponseDto getContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);

        Boolean b = contributor.getIsActive();
        if (Boolean.FALSE.equals(b)) {
            throw new ContributorIsDeactivateException();
        }

        return contributorMapper.toContributorResponseDto(contributor);
    }

    /**
     * 특정 ID의 기여자 정보를 수정하는 메서드입니다.
     *
     * @param contributorId 수정할 기여자의 ID
     * @param dto 수정할 기여자 정보가 담긴 ContributorRequestDto
     * @return 수정된 기여자의 정보를 담은 ContributorResponseDto
     * @throws ContributorNotFoundException 기여자를 찾을 수 없을 경우 발생
     * @throws ContributorRoleNotFoundException 기여자 역할을 찾을 수 없을 경우 발생
     * @throws ContributorIsDeactivateException 기여자가 비활성화 상태인 경우 발생
     */
    @Override
    @Transactional
    public ContributorResponseDto updateContributor(Long contributorId, ContributorRequestDto dto) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);

        ContributorRole contributorRole = contributorRoleRepository.findById(dto.contributorRoleId())
                .orElseThrow(ContributorRoleNotFoundException::new);

        Boolean b = contributor.getIsActive();
        if (Boolean.FALSE.equals(b)) {
            throw new ContributorIsDeactivateException();
        }

        contributor.toEntity(dto, contributorRole);
        Contributor updatedContributor = contributorRepository.save(contributor);

        return contributorMapper.toContributorResponseDto(updatedContributor);
    }

    /**
     * 특정 ID의 기여자를 비활성화하는 메서드입니다.
     *
     * @param contributorId 비활성화할 기여자의 ID
     * @throws ContributorNotFoundException 기여자를 찾을 수 없을 경우 발생
     */
    @Override
    @Transactional
    public void deactivateContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);
        contributor.deactivate();
        contributorRepository.save(contributor);
    }

    /**
     * 특정 ID의 비활성화된 기여자를 활성화하는 메서드입니다.
     *
     * @param contributorId 활성화할 기여자의 ID
     * @throws ContributorNotFoundException 기여자를 찾을 수 없을 경우 발생
     * @throws ContributorIsDeactivateException 기여자가 이미 활성화 상태일 경우 발생
     */
    @Override
    @Transactional
    public void activateContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(ContributorNotFoundException::new);
        contributor.activate();
        contributorRepository.save(contributor);
    }

    @Override
    @Transactional
    public List<ContributorNameRoleResponseDto> getActiveContributorsWithRoles() {
        return contributorRepository.findContributorsWithRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContributorIsActiveResponseDto> getAllContributors(Pageable pageable) {
        return contributorRepository.findAll(pageable)
                .map(contributorMapper::toContributorIsActiveResponseDto);
    }
}
