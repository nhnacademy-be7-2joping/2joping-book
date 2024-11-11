package com.nhnacademy.bookstore.coupon.controller;

import com.nhnacademy.bookstore.coupon.dto.request.CouponRequestDto;
import com.nhnacademy.bookstore.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.bookstore.coupon.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.*;

/**
 * CouponControllerTest
 *
 * 이 클래스는 CouponController의 REST API를 테스트합니다.
 *
 * @author Luha
 * @since 1.0
 */
public class CouponControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CouponService couponService;

    @InjectMocks
    private CouponController couponController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(couponController).build();
    }

    /**
     * 쿠폰 생성 성공 테스트
     * 새로운 쿠폰 생성 요청이 성공적으로 처리되고 예상된 데이터를 반환하는지 확인합니다.
     *
     * @throws Exception MockMvc 요청 시 발생할 수 있는 예외
     * @since 1.0
     * @author Luha
     */
    @Test
    void createCoupon_Success() throws Exception {
        // given
        CouponRequestDto requestDto = new CouponRequestDto(1L, "New Coupon", LocalDate.now());
        CouponResponseDto responseDto = new CouponResponseDto(1L, "New Coupon", LocalDate.now(), LocalDate.now().plusDays(30), 10, null);

        when(couponService.create(any(CouponRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"couponPolicyId\": 1, \"name\": \"New Coupon\", \"expiredAt\": \"2024-12-31\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Coupon")))
                .andExpect(jsonPath("$.couponId", is(1)))
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    /**
     * 쿠폰 생성 실패 테스트
     * 유효하지 않은 쿠폰 데이터로 요청 시 BadRequest가 발생하는지 확인합니다.
     *
     * @throws Exception MockMvc 요청 시 발생할 수 있는 예외
     * @since 1.0
     * @author Luha
     */
    @Test
    void createCoupon_BadRequest() throws Exception {
        // given
        CouponRequestDto requestDto = new CouponRequestDto(null, "", LocalDate.now());

        // when & then
        mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"couponPolicyId\": null, \"name\": \"\", \"expiredAt\": \"\" }"))
                .andExpect(status().isBadRequest());
    }

    /**
     * 모든 쿠폰 조회 성공 테스트
     * 쿠폰 목록 요청이 성공적으로 처리되고 예상된 쿠폰 데이터 목록이 반환되는지 확인합니다.
     *
     * @throws Exception MockMvc 요청 시 발생할 수 있는 예외
     * @since 1.0
     * @author Luha
     */
    @Test
    void getAllCoupons_Success() throws Exception {
        // given
        CouponResponseDto responseDto = new CouponResponseDto(1L, "Sample Coupon", LocalDate.now(), LocalDate.now().plusDays(10), 5, null);
        when(couponService.getAllCouponse()).thenReturn(Collections.singletonList(responseDto));

        // when & then
        mockMvc.perform(get("/api/v1/coupons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Sample Coupon")))
                .andExpect(jsonPath("$[0].couponId", is(1)))
                .andExpect(jsonPath("$[0].quantity", is(5)));
    }

    /**
     * 모든 쿠폰 조회 시 빈 목록 반환 테스트
     * 쿠폰이 없을 경우 빈 목록이 반환되는지 확인합니다.
     *
     * @throws Exception MockMvc 요청 시 발생할 수 있는 예외
     * @since 1.0
     * author Luha
     */
    @Test
    void getAllCoupons_EmptyList() throws Exception {
        // given
        when(couponService.getAllCouponse()).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/v1/coupons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
