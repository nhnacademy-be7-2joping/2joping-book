package com.nhnacademy.bookstore.bookset.contributor.service.impl;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.mapper.ContributorMapper;
import com.nhnacademy.bookstore.bookset.contributor.mapper.ContributorRoleMapper;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContributorRoleServiceImplTest {

    @Mock
    private ContributorRoleRepository contributorRoleRepository;

    @Mock
    private ContributorMapper contributorMapper;

    @Mock
    private ContributorRoleMapper contributorRoleMapper;


    @InjectMocks
    private ContributorRoleServiceImpl contributorRoleService;

    @Test
    @DisplayName("기여자 역할 생성 테스트")
    void createContributorRole() {
        // given
        ContributorRoleRequestDto requestDto = new ContributorRoleRequestDto("지은이");
        ContributorRole savedRole = new ContributorRole(1L, "지은이");
        ContributorRoleResponseDto responseDto = new ContributorRoleResponseDto(1L, "지은이");

        when(contributorRoleRepository.save(any(ContributorRole.class))).thenReturn(savedRole);
        when(contributorRoleMapper.toContributorRoleResponseDto(savedRole)).thenReturn(responseDto);

        // when
        ContributorRoleResponseDto result = contributorRoleService.createContributorRole(requestDto);

        // then
        assertNotNull(result);
        assertEquals(1L, result.contributorRoleId());
        assertEquals("지은이", result.name());
    }

    @Test
    @DisplayName("기여자 역할 조회 테스트")
    void getContributorRole() {
        // given
        ContributorRole contributorRole = new ContributorRole(1L, "지은이");
        ContributorRoleResponseDto responseDto = new ContributorRoleResponseDto(1L, "지은이");

        when(contributorRoleRepository.findById(1L)).thenReturn(Optional.of(contributorRole));
        when(contributorRoleMapper.toContributorRoleResponseDto(contributorRole)).thenReturn(responseDto);

        // when
        ContributorRoleResponseDto result = contributorRoleService.getContributorRole(1L);

        // then
        assertNotNull(result);
        assertEquals(1L, result.contributorRoleId());
        assertEquals("지은이", result.name());
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
        ContributorRoleRequestDto requestDto = new ContributorRoleRequestDto("옮긴이");
        ContributorRole existingRole = new ContributorRole(1L, "지은이");
        ContributorRole updatedRole = new ContributorRole(1L, "옮긴이");
        ContributorRoleResponseDto responseDto = new ContributorRoleResponseDto(1L, "옮긴이");

        when(contributorRoleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(contributorRoleRepository.save(any(ContributorRole.class))).thenReturn(updatedRole);
        when(contributorRoleMapper.toContributorRoleResponseDto(updatedRole)).thenReturn(responseDto);

        // when
        ContributorRoleResponseDto result = contributorRoleService.updateContributorRole(1L, requestDto);

        // then
        assertNotNull(result);
        assertEquals(1L, result.contributorRoleId());
        assertEquals("옮긴이", result.name());
    }

    @Test
    @DisplayName("존재하지 않는 기여자 역할 수정시 예외 발생 테스트")
    void updateContributorRoleNotFound() {
        // given
        ContributorRoleRequestDto requestDto = new ContributorRoleRequestDto("옮긴이");
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

    @Test
    @DisplayName("전체 기여자 역할 리스트 조회 테스트")
    void getAllContributorRoles() {
        // given
        List<ContributorRole> roles = List.of(
                new ContributorRole(1L, "지은이"),
                new ContributorRole(2L, "옮긴이")
        );
        List<ContributorRoleResponseDto> expectedResponse = List.of(
                new ContributorRoleResponseDto(1L, "지은이"),
                new ContributorRoleResponseDto(2L, "옮긴이")
        );

        when(contributorRoleRepository.findAll()).thenReturn(roles);
        when(contributorRoleMapper.toContributorRoleResponseDto(roles.get(0)))
                .thenReturn(expectedResponse.get(0));
        when(contributorRoleMapper.toContributorRoleResponseDto(roles.get(1)))
                .thenReturn(expectedResponse.get(1));

        // when
        List<ContributorRoleResponseDto> actualResponse = contributorRoleService.getAllContributorRoles();

        // then
        assertNotNull(actualResponse);
        assertEquals(2, actualResponse.size());
        assertEquals(expectedResponse, actualResponse);
    }
}
