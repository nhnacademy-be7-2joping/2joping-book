package com.nhnacademy.bookstore.point.service;

import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageDetailPointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageSimplePointHistoriesResponse;

public interface PointService {

    void awardReviewPoint(Long customerId, Long orderDetailId);
    void awardOrderPoint(Long customerId, Long orderId);
    void usePoint(PointUseRequest request);

    GetMyPageSimplePointHistoriesResponse getMyPageSimplePointHistories(Long customerId);
    GetMyPageDetailPointHistoriesResponse getMyPageDetailPointHistories(Long customerId);
}
