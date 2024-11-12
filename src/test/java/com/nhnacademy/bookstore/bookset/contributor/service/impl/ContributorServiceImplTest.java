package com.nhnacademy.bookstore.bookset.contributor.service.impl;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.mapper.ContributorMapper;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRepository;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorIsDeactivateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContributorServiceImplTest {

    @Mock
    private ContributorRepository contributorRepository;

    @Mock
    private ContributorRoleRepository contributorRoleRepository;

    @Mock
    private ContributorMapper contributorMapper;

    @InjectMocks
    private ContributorServiceImpl contributorService;

    @Test
    @DisplayName("도서 기여자 생성 테스트")
    void createContributor() {
        // given
        ContributorRole contributorRole = new ContributorRole(1L, "작가");
        ContributorRequestDto requestDto = new ContributorRequestDto("삼조핑", 1L);
        Contributor savedContributor = new Contributor(null, contributorRole, "삼조핑", true);

        when(contributorRoleRepository.findById(1L)).thenReturn(Optional.of(contributorRole));
        when(contributorRepository.save(any(Contributor.class))).thenReturn(savedContributor);
        when(contributorMapper.toContributorResponseDto(any(Contributor.class)))
                .thenReturn(new ContributorResponseDto(1L, 1L, "삼조핑"));

        // when
        ContributorResponseDto responseDto = contributorService.createContributor(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.contributorId());
        assertEquals(1L, responseDto.contributorRoleId());
        assertEquals("삼조핑", responseDto.name());
    }

    @Test
    @DisplayName("도서 기여자 역할이 없는 경우 예외 발생 테스트")
    void createContributorRoleNotFound() {
        // given
        ContributorRequestDto requestDto = new ContributorRequestDto("삼조핑", 1L);

        when(contributorRoleRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContributorRoleNotFoundException.class, () -> contributorService.createContributor(requestDto));
    }

    @Test
    @DisplayName("도서 기여자 조회 테스트")
    void getContributor() {
        // given
        ContributorRole contributorRole = new ContributorRole(1L, "작가");
        Contributor contributor = new Contributor(1L, contributorRole, "삼조핑", true);

        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));
        when(contributorMapper.toContributorResponseDto(any(Contributor.class)))
                .thenReturn(new ContributorResponseDto(1L, 1L, "삼조핑"));

        // when
        ContributorResponseDto responseDto = contributorService.getContributor(1L);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.contributorId());
        assertEquals(1L, responseDto.contributorRoleId());
        assertEquals("삼조핑", responseDto.name());
    }


    @Test
    @DisplayName("비활성화된 도서 기여자 조회 테스트")
    void getDeactivatedContributor() {
        // given
        ContributorRole contributorRole = new ContributorRole(1L, "작가");
        Contributor deactivatedContributor = new Contributor(1L, contributorRole, "삼조핑", false);

        when(contributorRepository.findById(1L)).thenReturn(Optional.of(deactivatedContributor));

        // when & then
        assertThrows(ContributorIsDeactivateException.class, () -> contributorService.getContributor(1L));
    }


    @Test
    @DisplayName("존재하지 않는 도서 기여자 조회 시 예외 발생 테스트")
    void getContributorNotFound() {
        // given
        when(contributorRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContributorNotFoundException.class, () -> contributorService.getContributor(1L));
    }

    @Test
    @DisplayName("도서 기여자 수정 테스트")
    void updateContributor() {
        // given
        ContributorRole newRole = new ContributorRole(2L, "Editor");
        Contributor contributor = new Contributor(1L, new ContributorRole(1L, "작가"), "삼조핑", true);
        ContributorRequestDto requestDto = new ContributorRequestDto("이조핑", 2L);

        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));
        when(contributorRoleRepository.findById(2L)).thenReturn(Optional.of(newRole));
        when(contributorRepository.save(any(Contributor.class))).thenReturn(contributor);
        when(contributorMapper.toContributorResponseDto(any(Contributor.class)))
                .thenReturn(new ContributorResponseDto(1L, 2L, "이조핑"));

        // when
        ContributorResponseDto responseDto = contributorService.updateContributor(1L, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.contributorId());
        assertEquals(2L, responseDto.contributorRoleId());
        assertEquals("이조핑", responseDto.name());
    }

    @Test
    @DisplayName("도서 기여자 수정 시 역할을 찾지 못하는 경우 예외 발생 테스트")
    void updateContributorRoleNotFound() {
        // given
        Contributor contributor = new Contributor(1L, new ContributorRole(1L, "작가"), "삼조핑", true);
        ContributorRequestDto requestDto = new ContributorRequestDto("이조핑", 2L);

        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));
        when(contributorRoleRepository.findById(2L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContributorRoleNotFoundException.class, () -> contributorService.updateContributor(1L, requestDto));
    }

    @Test
    @DisplayName("도서 기여자 비활성화 테스트")
    void deactivateContributor() {
        // given
        Contributor contributor = new Contributor(1L, new ContributorRole(1L, "작가"), "삼조핑", true);

        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));
        when(contributorRepository.save(any(Contributor.class))).thenReturn(contributor);

        // when
        contributorService.deactivateContributor(1L);

        // then
        assertFalse(contributor.getIsActive());
        verify(contributorRepository, times(1)).save(contributor);
    }

    @Test
    @DisplayName("존재하지 않는 도서 기여자 비활성화 시 예외 발생 테스트")
    void deactivateContributorNotFound() {
        // given
        when(contributorRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContributorNotFoundException.class, () -> contributorService.deactivateContributor(1L));
    }

    @Test
    @DisplayName("비활성화된 도서 기여자 수정 시 예외 발생 테스트")
    void updateDeactivatedContributor() {
        // given
        ContributorRole contributorRole = new ContributorRole(1L, "작가");
        Contributor deactivatedContributor = new Contributor(1L, contributorRole, "삼조핑", false);
        ContributorRequestDto requestDto = new ContributorRequestDto("이조핑", 1L);

        when(contributorRepository.findById(1L)).thenReturn(Optional.of(deactivatedContributor));
        when(contributorRoleRepository.findById(1L)).thenReturn(Optional.of(contributorRole));

        // when & then
        assertThrows(ContributorIsDeactivateException.class, () -> contributorService.updateContributor(1L, requestDto));
    }

    @Test
    @DisplayName("도서 기여자 활성화 테스트")
    void activateContributor() {
        // given
        Contributor contributor = new Contributor(1L, new ContributorRole(1L, "작가"), "삼조핑", false);

        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));
        when(contributorRepository.save(any(Contributor.class))).thenReturn(contributor);

        // when
        contributorService.activateContributor(1L);

        // then
        assertTrue(contributor.getIsActive());
        verify(contributorRepository, times(1)).save(contributor);
    }
}
