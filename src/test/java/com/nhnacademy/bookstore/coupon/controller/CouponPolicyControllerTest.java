package com.nhnacademy.bookstore.coupon.controller;


import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.*;

/**
 * CouponPolicyControllerTest
 *
 * 이 클래스는 CouponPolicyController의 REST API를 테스트합니다.
 * 쿠폰 정책 조회 기능에 대한 테스트를 포함합니다.
 *
 * @since 1.0
 * @author Luha
 */
class CouponPolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CouponPolicyService couponPolicyService;

    @InjectMocks
    private CouponPolicyController couponPolicyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(couponPolicyController).build();
    }

    /**
     * 모든 쿠폰 정책 조회 성공 테스트
     * 쿠폰 정책 목록 요청이 성공적으로 처리되고 예상된 쿠폰 정책 데이터 목록이 반환되는지 확인합니다.
     *
     * @throws Exception MockMvc 요청 시 발생할 수 있는 예외
     * @author Luha
     * @since 1.0
     */
    @Test
    void getAllPolicies_Success() throws Exception {
        // given
        CouponPolicyResponseDto responseDto = new CouponPolicyResponseDto(1L, "Discount Policy", "PERCENTAGE", 10, 100, 30, "Policy Detail", 50);
        when(couponPolicyService.getAllCouponPolicies()).thenReturn(Collections.singletonList(responseDto));

        // when & then
        mockMvc.perform(get("/api/v1/coupon/policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Discount Policy")))
                .andExpect(jsonPath("$[0].couponPolicyId", is(1)))
                .andExpect(jsonPath("$[0].discountType", is("PERCENTAGE")));
    }

    /**
     * 모든 쿠폰 정책 조회 시 빈 목록 반환 테스트
     * 쿠폰 정책이 없을 경우 빈 목록이 반환되는지 확인합니다.
     *
     * @throws Exception MockMvc 요청 시 발생할 수 있는 예외
     * @since 1.0
     * author Luha
     */
    @Test
    void getAllPolicies_EmptyList() throws Exception {
        // given
        when(couponPolicyService.getAllCouponPolicies()).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/v1/coupon/policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}