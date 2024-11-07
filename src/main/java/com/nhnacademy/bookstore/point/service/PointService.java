package com.nhnacademy.bookstore.point.service;

import com.nhnacademy.bookstore.common.error.exception.point.SignUpPointPolicyNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.point.dto.request.PointHistoryDto;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.entity.PointHistory;
import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import com.nhnacademy.bookstore.point.repository.PointHistoryRepository;
import com.nhnacademy.bookstore.point.repository.PointTypeRepository;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointService {

    private final PointTypeRepository pointTypeRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void awardSignUpPoint(Long customerId) {
        PointType signUpPointType = pointTypeRepository.findByNameAndIsActiveTrue("회원가입")
                .orElseThrow(() -> new SignUpPointPolicyNotFoundException("회원가입 포인트 정책을 찾을 수 없습니다."));

        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));

        Integer pointAmount = signUpPointType.getAccVal();
        // TODO: 회원가입 시 해당 멤버의 포인트 +5000 로직 추가
    }

    @Transactional
    public void awardReviewPoint(Long customerId, Long orderDetailId) {
        PointType reviewPointType = pointTypeRepository.findByNameAndIsActiveTrue("리뷰작성")
                .orElseThrow(() -> new EntityNotFoundException("리뷰 포인트 정책을 찾을 수 없습니다."));

        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        Integer pointAmount = reviewPointType.getAccVal();
        // TODO: 리뷰 작성 시 해당 멤버의 포인트 +500 로직 추가

        createPointHistory(reviewPointType, orderDetailId, null, customerId, pointAmount);
    }

    // TODO: 구매 시 적립 포인트 관련 메서드

    @Transactional
    public void usePoint(PointUseRequest request) {
        Member member = memberRepository.findById(request.customerId())
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));

        // TODO: 주문 시에 포인트 사용할 경우 포인트 사용 요청에 담겨 들어온 포인트 양만큼 멤버 포인트 삭제
//        member.usePoint(request.pointAmount());

        PointType usePointType = pointTypeRepository.findByNameAndIsActiveTrue("포인트사용")
                .orElseThrow(() -> new EntityNotFoundException("포인트 사용 정책을 찾을 수 없습니다."));

        createPointHistory(usePointType, null, request.orderId(),
                request.customerId(), -request.pointAmount());
    }

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
}
