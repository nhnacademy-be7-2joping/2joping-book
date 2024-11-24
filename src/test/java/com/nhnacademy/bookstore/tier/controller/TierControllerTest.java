package com.nhnacademy.bookstore.tier.controller;

import com.nhnacademy.bookstore.user.tier.controller.TierController;
import com.nhnacademy.bookstore.user.tier.dto.response.MemberTierResponse;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import com.nhnacademy.bookstore.user.tier.service.TierService;
import org.junit.jupiter.api.BeforeEach;
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

class TierControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TierService tierService;

    @InjectMocks
    private TierController tierController;

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