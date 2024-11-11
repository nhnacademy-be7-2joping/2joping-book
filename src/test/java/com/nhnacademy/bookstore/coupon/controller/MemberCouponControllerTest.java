package com.nhnacademy.bookstore.coupon.controller;


import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.service.MemberCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.*;

/**
 * MemberCouponControllerTest
 *
 * 이 클래스는 MemberCouponController의 REST API를 테스트합니다.
 * 회원이 보유한 쿠폰 목록 조회 기능에 대한 테스트를 포함합니다.
 *
 * @since 1.0
 * author Luha
 */
class MemberCouponControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberCouponService memberCouponService;

    @InjectMocks
    private MemberCouponController memberCouponController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberCouponController).build();
    }

    /**
     * 특정 회원의 모든 쿠폰 조회 성공 테스트
     * 회원 ID로 쿠폰 목록 요청이 성공적으로 처리되고 예상된 쿠폰 데이터 목록이 반환되는지 확인합니다.
     *
     * @throws Exception MockMvc 요청 시 발생할 수 있는 예외
     * @since 1.0
     * author Luha
     */
    @Test
    void getCouponsByMemberId_Success() throws Exception {
        // given
        MemberCouponResponseDto responseDto = new MemberCouponResponseDto(1L, 2L, LocalDateTime.now(), LocalDateTime.now().plusDays(10), false, null, null);
        when(memberCouponService.getAllMemberCoupons(1L)).thenReturn(Collections.singletonList(responseDto));

        // when & then
        mockMvc.perform(get("/api/v1/coupons/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].couponUsageId", is(1)))
                .andExpect(jsonPath("$[0].couponId", is(2)))
                .andExpect(jsonPath("$[0].isUsed", is(false)));
    }

    /**
     * 특정 회원의 쿠폰이 없을 때 빈 목록 반환 테스트
     * 회원 ID로 조회 시 쿠폰이 없을 경우 빈 목록이 반환되는지 확인합니다.
     *
     * @throws Exception MockMvc 요청 시 발생할 수 있는 예외
     * @since 1.0
     * author Luha
     */
    @Test
    void getCouponsByMemberId_EmptyList() throws Exception {
        // given
        when(memberCouponService.getAllMemberCoupons(1L)).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/v1/coupons/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}