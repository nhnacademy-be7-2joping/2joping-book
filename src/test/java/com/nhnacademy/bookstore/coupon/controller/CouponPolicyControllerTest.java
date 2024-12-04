package com.nhnacademy.bookstore.coupon.controller;


import com.nhnacademy.bookstore.coupon.dto.request.CreateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.request.UpdateCouponPolicyRequest;
import com.nhnacademy.bookstore.coupon.dto.response.CouponPolicyResponseDto;
import com.nhnacademy.bookstore.coupon.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CouponPolicyControllerTest
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
    @DisplayName("모든 쿠폰 정책 조회 - 성공")
    void getAllPolicies_Success() throws Exception {
        // given
        CouponPolicyResponseDto responseDto = new CouponPolicyResponseDto(1L, "Discount Policy", "PERCENTAGE", 10, 100, 30, "Policy Detail", 50);
        when(couponPolicyService.getAllCouponPolicies()).thenReturn(Collections.singletonList(responseDto));

        // when & then
        mockMvc.perform(get("/api/v1/coupon/policies/activated")
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
    @DisplayName("모든 쿠폰 정책 조회 - 빈 목록 반환")
    void getAllPolicies_EmptyList() throws Exception {
        // given
        when(couponPolicyService.getAllCouponPolicies()).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/v1/coupon/policies/activated")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * 테스트: 쿠폰 정책 생성 - 성공
     * 예상 결과: 쿠폰 정책이 성공적으로 생성되고 생성된 ID가 반환됨.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 생성 - 성공")
    void createCouponPolicy_Success() throws Exception {
        // Given
        Long expectedCouponPolicyId = 1L;

        when(couponPolicyService.createCouponPolicy(any(CreateCouponPolicyRequest.class))).thenReturn(expectedCouponPolicyId);

        // When & Then
        mockMvc.perform(post("/api/v1/coupon/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Test Policy",
                          "discountType": "PERCENT",
                          "discountValue": 10,
                          "usageLimit": 100,
                          "duration": 30,
                          "detail": "Detailed Policy Description",
                          "maxDiscount": 50,
                          "isActive": true
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/" + expectedCouponPolicyId));
    }

    /**
     * 테스트: 특정 쿠폰 정책 조회 - 성공
     * 예상 결과: 요청한 쿠폰 정책의 세부 정보가 반환됨.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("특정 쿠폰 정책 조회 - 성공")
    void getCouponPolicy_Success() throws Exception {
        // Given
        Long couponPolicyId = 1L;

        CouponPolicyResponseDto responseDto = new CouponPolicyResponseDto(
                couponPolicyId,
                "Discount Policy",
                "PERCENTAGE",
                10,
                100,
                30,
                "Policy Detail",
                50
        );

        when(couponPolicyService.getCouponPolicy(couponPolicyId)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(get("/api/v1/coupon/policies/{coupon-policy-id}", couponPolicyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.couponPolicyId", is(1)))
                .andExpect(jsonPath("$.name", is("Discount Policy")))
                .andExpect(jsonPath("$.discountType", is("PERCENTAGE")))
                .andExpect(jsonPath("$.discountValue", is(10)))
                .andExpect(jsonPath("$.maxDiscount", is(50)))
                .andExpect(jsonPath("$.duration", is(30)))
                .andExpect(jsonPath("$.detail", is("Policy Detail")));
    }

    /**
     * 테스트: 쿠폰 정책 수정 - 성공
     * 예상 결과: 요청한 ID의 쿠폰 정책이 성공적으로 수정됨.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 수정 - 성공")
    void updateCouponPolicy_Success() throws Exception {
        // Given
        Long couponPolicyId = 1L;


        when(couponPolicyService.updateCouponPolicy(any(Long.class), any(UpdateCouponPolicyRequest.class)))
                .thenReturn(couponPolicyId);

        // When & Then
        mockMvc.perform(put("/api/v1/coupon/policies/{coupon-policy-id}", couponPolicyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Updated Policy",
                      "discountType": "PERCENT",
                      "discountValue": 20,
                      "usageLimit": 200,
                      "duration": 60,
                      "detail": "Updated Policy Description",
                      "maxDiscount": 100,
                      "isActive": true
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(couponPolicyId.toString())); // ResponseEntity.ok()의 값 검증
    }

    /**
     * 테스트: 쿠폰 정책 비활성화 - 성공
     * 예상 결과: 요청한 쿠폰 정책이 성공적으로 비활성화됨.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("쿠폰 정책 비활성화 - 성공")
    void deleteCouponPolicy_Success() throws Exception {
        // Given
        Long couponPolicyId = 1L;

        // couponPolicyService.deleteCouponPolicy 호출 시 아무 동작도 하지 않도록 설정
        doNothing().when(couponPolicyService).deleteCouponPolicy(couponPolicyId);

        // When & Then
        mockMvc.perform(put("/api/v1/coupon/policies/{coupon-policy-id}/deactivated", couponPolicyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // ResponseEntity.noContent() 응답 확인
    }

}