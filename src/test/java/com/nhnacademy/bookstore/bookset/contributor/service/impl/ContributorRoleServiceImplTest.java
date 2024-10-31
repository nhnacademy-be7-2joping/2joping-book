package com.nhnacademy.bookstore.bookset.contributor.service.impl;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContributorRoleServiceImplTest {

    @Mock
    private ContributorRoleRepository contributorRoleRepository;

    @InjectMocks
    private ContributorRoleServiceImpl contributorRoleService;

    @Test
    @DisplayName("기여자 역할 생성 테스트")
    void createContributorRole() {
        // given
        ContributorRoleRequestDto requestDto = new ContributorRoleRequestDto();
        requestDto.setRoleName("작가");

        ContributorRole savedRole = new ContributorRole(1L, "작가");
        when(contributorRoleRepository.save(any(ContributorRole.class))).thenReturn(savedRole);

        // when
        ContributorRoleResponseDto responseDto = contributorRoleService.createContributorRole(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getContributorRoleId());
        assertEquals("작가", responseDto.getRoleName());
    }

    @Test
    @DisplayName("기여자 역할 조회 테스트")
    void getContributorRole() {
        // given
        ContributorRole contributorRole = new ContributorRole(1L, "작가");
        when(contributorRoleRepository.findById(1L)).thenReturn(Optional.of(contributorRole));

        // when
        ContributorRoleResponseDto responseDto = contributorRoleService.getContributorRole(1L);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getContributorRoleId());
        assertEquals("작가", responseDto.getRoleName());
    }

    @Test
    @DisplayName("존재하지 않는 기여자 역할 조회시 예외 발생 테스트")
    void getContributorRoleNotFound() {
        // given
        when(contributorRoleRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContributorRoleNotFoundException.class, () -> contributorRoleService.getContributorRole(1L));
    }

    @Test
    @DisplayName("기여자 역할 수정 테스트")
    void updateContributorRole() {
        // given
        ContributorRoleRequestDto requestDto = new ContributorRoleRequestDto();
        requestDto.setRoleName("편집자");

        ContributorRole existingRole = new ContributorRole(1L, "작가");
        when(contributorRoleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(contributorRoleRepository.save(any(ContributorRole.class))).thenReturn(new ContributorRole(1L, "편집자"));

        // when
        ContributorRoleResponseDto responseDto = contributorRoleService.updateContributorRole(1L, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getContributorRoleId());
        assertEquals("편집자", responseDto.getRoleName());
    }

    @Test
    @DisplayName("존재하지 않는 기여자 역할 수정시 예외 발생 테스트")
    void updateContributorRoleNotFound() {
        // given
        ContributorRoleRequestDto requestDto = new ContributorRoleRequestDto();
        requestDto.setRoleName("편집자");
        when(contributorRoleRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ContributorRoleNotFoundException.class, () -> contributorRoleService.updateContributorRole(1L, requestDto));
    }

    @Test
    @DisplayName("기여자 역할 삭제 테스트")
    void deleteContributorRole() {
        // given
        when(contributorRoleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(contributorRoleRepository).deleteById(1L);

        // when
        contributorRoleService.deleteContributorRole(1L);

        // then
        verify(contributorRoleRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 기여자 역할 삭제시 예외 발생 테스트")
    void deleteContributorRoleNotFound() {
        // given
        when(contributorRoleRepository.existsById(1L)).thenReturn(false);

        // when & then
        assertThrows(ContributorRoleNotFoundException.class, () -> contributorRoleService.deleteContributorRole(1L));
    }
}
