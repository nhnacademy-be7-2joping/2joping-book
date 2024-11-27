package com.nhnacademy.bookstore.point.service.impl;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.dto.response.GetDetailPointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageDetailPointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageSimplePointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetSimplePointHistoriesResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointTypeRepository pointTypeRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

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
        memberRepository.save(member);
    }

    @Override
    public void awardOrderPoint(Long customerId, Long orderId) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        MemberTier memberTier = member.getTier();
        Tier tierName = memberTier.getName();

        Order order = orderRepository.findByOrderId(orderId);

        int pointAmount = getPointAmount(order, String.valueOf(tierName), member);

        member.addPoint(pointAmount);
        memberRepository.save(member);
    }

    @Override
    public void usePoint(PointUseRequest request) {
        Member member = memberRepository.findById(request.customerId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        member.usePoint(request.pointAmount());

        PointType usePointType = pointTypeRepository.findByNameAndIsActiveTrue("포인트사용")
                .orElseThrow(() -> new EntityNotFoundException("포인트 사용 정책을 찾을 수 없습니다."));

        createPointHistory(usePointType, null, request.orderId(),
                request.customerId(), -request.pointAmount());
    }

    /**
     * @param customerId
     * @return 회원 페이지 포인트 간략 정보
     */
    @Override
    public GetMyPageSimplePointHistoriesResponse getMyPageSimplePointHistories(Long customerId) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        List<GetSimplePointHistoriesResponse> responses = pointHistoryRepository
                .findAllByCustomerIdOrderByRegisterDateDesc(customerId)
                .stream()
                .map(GetSimplePointHistoriesResponse::from)
                .toList();

        return GetMyPageSimplePointHistoriesResponse.of(member, responses);
    }

    /**
     * @param customerId
     * @return 회원 페이지 포인트 상세 정보
     */
    @Override
    public GetMyPageDetailPointHistoriesResponse getMyPageDetailPointHistories(Long customerId) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        List<GetDetailPointHistoriesResponse> responses = pointHistoryRepository
                .findAllByCustomerIdOrderByRegisterDateDesc(customerId)
                .stream()
                .map(GetDetailPointHistoriesResponse::from)
                .toList();

        return GetMyPageDetailPointHistoriesResponse.of(member, responses);
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
