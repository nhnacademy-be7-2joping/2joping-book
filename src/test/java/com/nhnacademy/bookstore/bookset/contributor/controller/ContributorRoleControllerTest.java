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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContributorRoleController.class)
@TestPropertySource(properties = "keymanager.url=http://localhost:8084")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/bookstore/contributors/role")
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
        mockMvc.perform(MockMvcRequestBuilders.get("/bookstore/contributors/role/1")
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
        mockMvc.perform(MockMvcRequestBuilders.put("/bookstore/contributors/role/1")
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/bookstore/contributors/role/1")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNoContent());
    }
}
