package com.nhnacademy.bookstore.point.service;

import com.nhnacademy.bookstore.point.dto.request.OrderPointAwardRequest;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageDetailPointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageSimplePointHistoriesResponse;

public interface PointService {

    void awardReviewPoint(Long customerId, Long orderDetailId);
    void awardOrderPoint(OrderPointAwardRequest request);
    void usePoint(PointUseRequest request);

    GetMyPageSimplePointHistoriesResponse getMyPageSimplePointHistories(Long customerId);
    GetMyPageDetailPointHistoriesResponse getMyPageDetailPointHistories(Long customerId);
}
