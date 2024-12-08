package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.response.GetMyPageDetailPointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageSimplePointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.service.impl.PointServiceImpl;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

    private final PointServiceImpl pointServiceImpl;
    private final PointTypeServiceImpl pointTypeServiceImpl;

    @GetMapping("/histories")
    public ResponseEntity<GetMyPageSimplePointHistoriesResponse> getSimplePointHistories(
            @RequestHeader("X-Customer-Id") String customerId
    ) {
        GetMyPageSimplePointHistoriesResponse response = pointServiceImpl.getMyPageSimplePointHistories(Long.parseLong(customerId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/histories/details")
    public ResponseEntity<GetMyPageDetailPointHistoriesResponse> getDetailPointHistories(
            @RequestHeader("X-Customer-Id") String customerId
    ) {
        GetMyPageDetailPointHistoriesResponse response = pointServiceImpl.getMyPageDetailPointHistories(Long.parseLong(customerId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/types")
    public ResponseEntity<List<GetPointTypeResponse>> getActivePointTypes() {
        return ResponseEntity.ok(pointTypeServiceImpl.getAllActivePointTypes());
    }
}
