package com.nhnacademy.bookstore.bookset.contributor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorRoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContributorRoleController.class)
class ContributorRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContributorRoleService contributorRoleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("기여자 역할 생성 테스트")
    void createContributorRole() throws Exception {
        // given
        ContributorRoleRequestDto requestDto = new ContributorRoleRequestDto("지은이");
        ContributorRoleResponseDto responseDto = new ContributorRoleResponseDto(1L, "지은이");

        Mockito.when(contributorRoleService.createContributorRole(any(ContributorRoleRequestDto.class)))
                .thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookstore/contributors/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contributorRoleId").value(1L))
                .andExpect(jsonPath("$.name").value("지은이"));
    }

    @Test
    @DisplayName("기여자 역할 조회 테스트")
    void getContributorRole() throws Exception {
        // given
        ContributorRoleResponseDto responseDto = new ContributorRoleResponseDto(1L, "지은이");

        Mockito.when(contributorRoleService.getContributorRole(1L)).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/contributors/role/1")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contributorRoleId").value(1L))
                .andExpect(jsonPath("$.name").value("지은이"));
    }

    @Test
    @DisplayName("기여자 역할 수정 테스트")
    void updateContributorRole() throws Exception {
        // given
        ContributorRoleRequestDto requestDto = new ContributorRoleRequestDto("옮긴이");
        ContributorRoleResponseDto responseDto = new ContributorRoleResponseDto(1L, "옮긴이");

        Mockito.when(contributorRoleService.updateContributorRole(eq(1L), any(ContributorRoleRequestDto.class)))
                .thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/contributors/role/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contributorRoleId").value(1L))
                .andExpect(jsonPath("$.name").value("옮긴이"));
    }

    @Test
    @DisplayName("기여자 역할 삭제 테스트")
    void deleteContributorRole() throws Exception {
        // given
        Mockito.doNothing().when(contributorRoleService).deleteContributorRole(1L);

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bookstore/contributors/role/1")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("전체 기여자 역할 리스트 조회 테스트")
    void getAllContributorRoles() throws Exception {
        // given
        List<ContributorRoleResponseDto> roles = List.of(
                new ContributorRoleResponseDto(1L, "지은이"),
                new ContributorRoleResponseDto(2L, "옮긴이")
        );

        Mockito.when(contributorRoleService.getAllContributorRoles()).thenReturn(roles);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/contributors/role")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].contributorRoleId").value(1L))
                .andExpect(jsonPath("$[0].name").value("지은이"))
                .andExpect(jsonPath("$[1].contributorRoleId").value(2L))
                .andExpect(jsonPath("$[1].name").value("옮긴이"));
    }
}
