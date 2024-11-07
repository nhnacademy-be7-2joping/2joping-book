package com.nhnacademy.bookstore.point.service;

import com.nhnacademy.bookstore.point.dto.request.PointHistoryDto;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;

import java.util.List;

public interface PointService {

    void awardSignUpPoint(Long customerId);
    void awardReviewPoint(Long customerId, Long orderDetailId);
    void usePoint(PointUseRequest request);
    List<PointHistoryDto> getPointHistory(Long customerId);
}
