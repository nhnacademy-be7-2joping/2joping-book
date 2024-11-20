package com.nhnacademy.bookstore.point.service.impl;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.point.SignUpPointPolicyNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.point.dto.request.PointHistoryDto;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.entity.PointHistory;
import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import com.nhnacademy.bookstore.point.repository.PointHistoryRepository;
import com.nhnacademy.bookstore.point.repository.PointTypeRepository;
import com.nhnacademy.bookstore.point.service.PointService;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointTypeRepository pointTypeRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public void awardReviewPoint(Long customerId, Long orderDetailId) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        PointType reviewPointType = pointTypeRepository.findByNameAndIsActiveTrue("리뷰작성")
                .orElseThrow(() -> new EntityNotFoundException("리뷰 포인트 정책을 찾을 수 없습니다."));

        int pointAmount = reviewPointType.getAccVal();

        member.addPoint(pointAmount);

        createPointHistory(reviewPointType, orderDetailId, null, customerId, pointAmount);
    }

    // TODO: 결제 시 적립 포인트 관련 메서드
    // 멤버가 주문 완료했을 때
    // 어떤 멤버인지 멤버 정보를 찾고
    // 어떤 주문인지 주문 정보를 찾고
    // 해당 주문에 대해서 구매 적립 포인트 추가
    // 추가 완료 return
    @Transactional
    @Override
    public void awardOrderPoint(Long customerId, Long orderId) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        MemberTier memberTier = member.getTier();
        Tier name = memberTier.getName();

        // 주문 정보를 통해서 총 가격 구하기
        // 해당 주문 정보에 대한 가격으로 구매 적립 포인트 추가
        // 순수금액 = 주문금액 - (쿠폰 + 배송비 + 취소금액 + 포장비)
        Order order = orderRepository.findByOrderId(orderId);

        int pointAmount = getPointAmount(order, String.valueOf(name), member);

        member.addPoint(pointAmount);
    }

    @Transactional
    @Override
    public void usePoint(PointUseRequest request) {
        Member member = memberRepository.findById(request.customerId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        // TODO: 주문 시에 포인트 사용할 경우 포인트 사용 요청에 담겨 들어온 포인트 양만큼 멤버 포인트 삭제
        member.usePoint(request.pointAmount());

        PointType usePointType = pointTypeRepository.findByNameAndIsActiveTrue("포인트사용")
                .orElseThrow(() -> new EntityNotFoundException("포인트 사용 정책을 찾을 수 없습니다."));

        createPointHistory(usePointType, null, request.orderId(),
                request.customerId(), -request.pointAmount());
    }

    @Override
    public List<PointHistoryDto> getPointHistory(Long customerId) {
        return pointHistoryRepository.findByCustomerId(customerId)
                .stream()
                .map(PointHistoryDto::from)
                .toList();
    }

    private Integer calculatePurchasePoint(Integer totalPrice, PointType pointType) {
        if (pointType.getType() == PointTypeEnum.PERCENT) {
            return (int) (totalPrice * (pointType.getAccVal() / 100.0));
        }
        return pointType.getAccVal();
    }

    private void createPointHistory(PointType pointType, Long orderDetailId,
                                    Long orderId, Long customerId, Integer pointVal) {
        PointHistory pointHistory = PointHistory.builder()
                .pointType(pointType)
                .orderDetailId(orderDetailId)
                .orderId(orderId)
                .customerId(customerId)
                .pointVal(pointVal)
                .build();

        pointHistoryRepository.save(pointHistory);
    }

    private static int getPointAmount(Order order, String tierName, Member member) {
        int totalPrice = order.getTotalPrice();
        int couponSalePrice = order.getCouponSalePrice();
        int shippingFee = order.getShippingFee();

        int pointAmount = totalPrice - (couponSalePrice + shippingFee);

        if (tierName.equals("일반")) {
            pointAmount = pointAmount * 1 / 100;
        } else if (member.getTier().equals("로얄")) {
            pointAmount = pointAmount * 2 / 100;
        } else if (member.getTier().equals("골드")) {
            pointAmount = pointAmount * 3 / 100;
        } else if (member.getTier().equals("플래티넘")) {
            pointAmount = pointAmount * 4 / 100;
        } else {
            pointAmount = 0;
        }
        return pointAmount;
    }
}
