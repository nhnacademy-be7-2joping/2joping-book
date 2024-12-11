package com.nhnacademy.bookstore.point.service.impl;

import com.nhnacademy.bookstore.common.error.exception.orderset.order.OrderNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.point.PointAmountException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.point.dto.request.OrderPointAwardRequest;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.dto.request.ReviewPointAwardRequest;
import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.repository.PointHistoryRepository;
import com.nhnacademy.bookstore.point.repository.PointTypeRepository;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PointServiceImplTest {

    @InjectMocks
    private PointServiceImpl pointService;

    @Mock
    private PointHistoryService pointHistoryService;

    @Mock
    private PointTypeRepository pointTypeRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void awardReviewPoint_Success() {
        Long customerId = 123L;
        Member member = Member.builder()
                .point(0) // 초기 포인트
                .build();

        PointType reviewPointType = PointType.builder()
                .accVal(100) // 포인트 액수 설정
                .isActive(true)
                .build();

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(pointTypeRepository.findByNameAndIsActiveTrue("리뷰작성")).thenReturn(Optional.of(reviewPointType));

        pointService.awardReviewPoint(new ReviewPointAwardRequest(customerId));

        assertEquals(100, member.getPoint()); // 포인트가 100으로 증가해야 함
    }

    @Test
    void awardReviewPoint_MemberNotFound() {
        Long customerId = 123L;

        when(memberRepository.findById(customerId)).thenReturn(Optional.empty());

        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> {
            pointService.awardReviewPoint(new ReviewPointAwardRequest(customerId));
        });

        assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void awardOrderPoint_Success() {
        Long customerId = 123L;

        MemberTier mockTier = mock(MemberTier.class);
        when(mockTier.getName()).thenReturn(Tier.NORMAL);

        Member member = Member.builder()
                .point(0) // 초기 포인트
                .tier(mockTier)
                .build();

        Order order = Order.builder()
                .orderId(1L)
                .totalPrice(10000) // 주문 총 가격
                .build();

        PointType orderPointType = PointType.builder()
                .accVal(10) // 포인트 액수 설정
                .isActive(true)
                .build();

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(orderRepository.findByOrderId(1L)).thenReturn(Optional.of(order));
        when(pointTypeRepository.findByNameAndIsActiveTrue("도서주문")).thenReturn(Optional.of(orderPointType));

        pointService.awardOrderPoint(new OrderPointAwardRequest(customerId, 1L));

        assertEquals(100, member.getPoint()); // 포인트가 100으로 증가해야 함
    }

    @Test
    void awardOrderPoint_OrderNotFound() {
        Long customerId = 123L;

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(new Member()));
        when(orderRepository.findByOrderId(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            pointService.awardOrderPoint(new OrderPointAwardRequest(customerId, 1L));
        });
    }

    @Test
    void usePoint_Success() {
        Long customerId = 123L;
        Member member = Member.builder()
                .point(200) // 초기 포인트
                .build();

        PointType pointUseType = PointType.builder()
                .name("포인트사용")
                .isActive(true)
                .build();

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(pointTypeRepository.findByNameAndIsActiveTrue("포인트사용"))
                .thenReturn(Optional.of(pointUseType));

        PointUseRequest request = new PointUseRequest(customerId, 100); // 사용할 포인트 100

        pointService.usePoint(request);

        assertEquals(100, member.getPoint()); // 포인트가 100으로 감소해야 함
    }

    @Test
    void usePoint_NotEnoughPoints() {
        Long customerId = 123L;
        Member member = Member.builder()
                .point(50) // 초기 포인트
                .build();

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));

        PointUseRequest request = new PointUseRequest(customerId, 100); // 사용할 포인트 100

        assertThrows(PointAmountException.class, () -> {
            pointService.usePoint(request);
        });
    }

    @Test
    void getMyPageSimplePointHistories_Success() {
        Long customerId = 123L;
        Member member = Member.builder()
                .build();

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));

        // 여기에 pointHistoryRepository의 메서드 Mock 추가
        // 예시: when(pointHistoryRepository.findAllByCustomerIdOrderByRegisterDateDesc(customerId)).thenReturn(...);

        pointService.getMyPageSimplePointHistories(customerId);
        // 결과 검증 로직 추가
    }

    @Test
    void getMyPageDetailPointHistories_Success() {
        Long customerId = 123L;
        Member member = Member.builder()
                .build();

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));

        // 여기에 pointHistoryRepository의 메서드 Mock 추가
        // 예시: when(pointHistoryRepository.findAllByCustomerIdOrderByRegisterDateDesc(customerId)).thenReturn(...);

        pointService.getMyPageDetailPointHistories(customerId);
        // 결과 검증 로직 추가
    }
}
