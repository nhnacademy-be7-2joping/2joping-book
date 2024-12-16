package com.nhnacademy.bookstore.point.service.impl;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.orderset.order.OrderNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.point.PointAmountException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.point.dto.request.*;
import com.nhnacademy.bookstore.point.dto.response.GetDetailPointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageDetailPointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageSimplePointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetSimplePointHistoriesResponse;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointHistoryService pointHistoryService;

    private final PointTypeRepository pointTypeRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void awardReviewPoint(ReviewPointAwardRequest request) {
        Member member = memberRepository.findById(request.customerId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        PointType reviewPointType = pointTypeRepository.findByNameAndIsActiveTrue("리뷰작성")
                .orElseThrow(() -> new EntityNotFoundException("리뷰 포인트 정책을 찾을 수 없습니다."));

        int pointAmount = reviewPointType.getAccVal();

        member.addPoint(pointAmount);
        Long reviewPointHistoryId = pointHistoryService.createReviewPointHistory(
                new CreateReviewPointHistoryRequest(
                        reviewPointType,
                        null,
                        null,
                        member.getId(),
                        pointAmount,
                        LocalDateTime.now()
                )
        );
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void awardOrderPoint(OrderPointAwardRequest request) {
        Member member = memberRepository.findById(request.customerId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        Order order = orderRepository.findByOrderId(request.orderId())
                .orElseThrow(OrderNotFoundException::new);

        MemberTier memberTier = member.getTier();
        Tier tierName = memberTier.getName();

        int pointAmount = getPointAmount(order, String.valueOf(tierName), member);

        PointType orderPointType = pointTypeRepository.findByNameAndIsActiveTrue("도서주문")
                .orElseThrow(() -> new EntityNotFoundException("도서주문 포인트 정책을 찾을 수 없습니다."));

        member.addPoint(pointAmount);
        Long orderPointHistoryId = pointHistoryService.createOrderPointHistory(
                new CreateOrderPointHistoryRequest(
                        orderPointType,
                        null,
                        order.getOrderId(),
                        member.getId(),
                        pointAmount,
                        LocalDateTime.now()
                )
        );
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void usePoint(PointUseRequest request) {
        Member member = memberRepository.findById(request.customerId())
                .orElseThrow(() -> new MemberNotFoundException(
                        "회원을 찾을 수 없습니다.",
                        RedirectType.NONE,
                        null
                ));

        int accPoint = member.getPoint();
        int usePointAmount = request.pointAmount();

        if (accPoint < usePointAmount) {
            throw new PointAmountException(
                    "포인트 사용량이 포인트 보유량보다 많습니다."
            );
        }

        PointType pointUseType = pointTypeRepository.findByNameAndIsActiveTrue("포인트사용")
                .orElseThrow(() -> new EntityNotFoundException("포인트 사용 정책을 찾을 수 없습니다."));

        member.usePoint(usePointAmount);
        pointHistoryService.createPointUseHistory(
                new CreatePointUseHistoryUseRequest(
                        pointUseType,
                        null,
                        null,
                        member.getId(),
                        usePointAmount,
                        LocalDateTime.now()
                )
        );
        memberRepository.save(member);
    }

    /**
     * @param customerId
     * @return 회원 페이지 포인트 간략 정보
     */
    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

    private int getPointAmount(Order order, String tierName, Member member) {
        int totalPrice = order.getTotalPrice();

        if (tierName.equals("일반")) {
            totalPrice = totalPrice * 1 / 100;
        } else if (member.getTier().equals("로얄")) {
            totalPrice = totalPrice * 2 / 100;
        } else if (member.getTier().equals("골드")) {
            totalPrice = totalPrice * 3 / 100;
        } else if (member.getTier().equals("플래티넘")) {
            totalPrice = totalPrice * 4 / 100;
        } else {
            totalPrice = 0;
        }
        return totalPrice;
    }

    private Integer calculatePurchasePoint(Integer totalPrice, PointType pointType) {
        if (pointType.getType() == PointTypeEnum.PERCENT) {
            return (int) (totalPrice * (pointType.getAccVal() / 100.0));
        }
        return pointType.getAccVal();
    }
}
