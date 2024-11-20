package com.nhnacademy.bookstore.point.service;

import com.nhnacademy.bookstore.point.dto.response.GetMyPageDetailPointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageSimplePointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetSimplePointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.PointHistoryDto;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;

import java.util.List;

public interface PointService {

    void awardReviewPoint(Long customerId, Long orderDetailId);
    void awardOrderPoint(Long customerId, Long orderId);
    void usePoint(PointUseRequest request);

    GetMyPageSimplePointHistoriesResponse getMyPageSimplePointHistories(Long customerId);
    GetMyPageDetailPointHistoriesResponse getMyPageDetailPointHistories(Long customerId);
}
