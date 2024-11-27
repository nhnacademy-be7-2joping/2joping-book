package com.nhnacademy.bookstore.bookset.contributor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorNameRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContributorController.class)
class ContributorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContributorService contributorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("기여자 생성 테스트")
    void createContributor() throws Exception {
        // given
        ContributorRequestDto requestDto = new ContributorRequestDto("이조핑", 1L);
        ContributorResponseDto responseDto = new ContributorResponseDto(1L, 1L, "이조핑");

        Mockito.when(contributorService.createContributor(any(ContributorRequestDto.class)))
                .thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookstore/contributors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contributorId").value(1L))
                .andExpect(jsonPath("$.name").value("이조핑"));
    }

    @Test
    @DisplayName("기여자 조회 테스트")
    void getContributor() throws Exception {
        // given
        ContributorResponseDto responseDto =  new ContributorResponseDto(1L, 1L, "이조핑");
        Mockito.when(contributorService.getContributor(1L)).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/contributors/1")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contributorId").value(1L))
                .andExpect(jsonPath("$.name").value("이조핑"));
    }

    @Test
    @DisplayName("기여자 수정 테스트")
    void updateContributor() throws Exception {
        // given
        ContributorRequestDto requestDto = new ContributorRequestDto("이조핑", 1L);
        ContributorResponseDto responseDto =new ContributorResponseDto(1L, 1L, "이조핑");

        Mockito.when(contributorService.updateContributor(eq(1L), any(ContributorRequestDto.class)))
                .thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/contributors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contributorId").value(1L))
                .andExpect(jsonPath("$.name").value("이조핑"));
    }

    @Test
    @DisplayName("기여자 비활성화 테스트")
    void deactivateContributor() throws Exception {
        // given
        Mockito.doNothing().when(contributorService).deactivateContributor(1L);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/contributors/1/deactivate")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("기여자 활성화 테스트")
    void activateContributor() throws Exception {
        // given
        Mockito.doNothing().when(contributorService).activateContributor(1L);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/contributors/1/activate")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("기여자 이름 및 역할 리스트 조회 테스트")
    void getActiveContributors() throws Exception {
        // given
        List<ContributorNameRoleResponseDto> responseDtos = List.of(
                new ContributorNameRoleResponseDto("이조핑", "지은이"),
                new ContributorNameRoleResponseDto("삼조핑", "엮은이")
        );

        Mockito.when(contributorService.getActiveContributorsWithRoles()).thenReturn(responseDtos);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/contributors/active")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].contributorName").value("이조핑"))
                .andExpect(jsonPath("$[0].contributorRole").value("지은이"))
                .andExpect(jsonPath("$[1].contributorName").value("삼조핑"))
                .andExpect(jsonPath("$[1].contributorRole").value("엮은이"));
    }

    @Test
    @DisplayName("모든 기여자 조회 테스트 (페이징)")
    void getAllContributors() throws Exception {
        // given
        Page<ContributorIsActiveResponseDto> contributorsPage = new PageImpl<>(
                List.of(
                        new ContributorIsActiveResponseDto(1L, 1L, "이조핑", true),
                        new ContributorIsActiveResponseDto(2L, 1L, "삼조핑", false)
                )
        );

        Mockito.when(contributorService.getAllContributors(any(Pageable.class))).thenReturn(contributorsPage);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/contributors")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].contributorId").value(1L))
                .andExpect(jsonPath("$.content[0].contributorRoleId").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("이조핑"))
                .andExpect(jsonPath("$.content[0].isActive").value(true))
                .andExpect(jsonPath("$.content[1].contributorId").value(2L))
                .andExpect(jsonPath("$.content[1].contributorRoleId").value(1L))
                .andExpect(jsonPath("$.content[1].name").value("삼조핑"))
                .andExpect(jsonPath("$.content[1].isActive").value(false));
    }
}


