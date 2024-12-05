package com.nhnacademy.bookstore.tier.controller;

import com.nhnacademy.bookstore.user.tier.controller.TierController;
import com.nhnacademy.bookstore.user.tier.dto.response.MemberTierResponse;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import com.nhnacademy.bookstore.user.tier.service.TierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.*;


/**
 * TierControllerTest
 * 이 클래스는 TierController의 REST API를 테스트합니다.
 * 회원 등급 정보를 조회하는 기능에 대한 단위 테스트를 포함합니다.
 *
 * @since 1.0
 * @author Luha
 */
class TierControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TierService tierService;

    @InjectMocks
    private TierController tierController;

    /**
     * 테스트 초기화
     * 테스트 실행 전에 MockMvc와 필요한 의존성을 초기화합니다.
     * @since 1.0
     * @author Luha
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tierController).build();
    }

    /**
     * 회원 등급 조회 성공 테스트
     * 회원 ID로 요청 시 예상된 등급 정보가 반환되는지 확인합니다.
     */
    @Test
    @DisplayName("회원 등급 조회 성공 테스트 - 유효한 회원 ID")
    void memberTier_Success() throws Exception {
        // given
        MemberTierResponse responseDto = new MemberTierResponse(10, Tier.GOLD, 15, 1);
        when(tierService.getMemberTier(1L)).thenReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/v1/members/tier")
                        .header("X-Customer-Id", "1") // 헤더로 customerId 전달
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tier", is("골드")))
                .andExpect(jsonPath("$.accRate", is(1)));
    }


}