package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageDetailPointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.GetMyPageSimplePointHistoriesResponse;
import com.nhnacademy.bookstore.point.dto.response.PointTypeDto;
import com.nhnacademy.bookstore.point.service.impl.PointServiceImpl;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

    private final PointServiceImpl pointServiceImpl;
    private final PointTypeServiceImpl pointTypeServiceImpl;

    @PostMapping("/uses")
    public ResponseEntity<Void> usePoint(@RequestBody @Valid PointUseRequest request) {
        pointServiceImpl.usePoint(request);
        return ResponseEntity.ok().build();
    }

    // TODO: 포인트 간략 정보
    @GetMapping("/histories")
    public ResponseEntity<GetMyPageSimplePointHistoriesResponse> getSimplePointHistories(
            @RequestHeader("X-Customer-Id") String customerId
    ) {
        GetMyPageSimplePointHistoriesResponse response = pointServiceImpl.getMyPageSimplePointHistories(Long.parseLong(customerId));
        return ResponseEntity.ok(response);
    }

    // TODO: 포인트 상세 정보
    @GetMapping("/histories/details")
    public ResponseEntity<GetMyPageDetailPointHistoriesResponse> getDetailPointHistories(
            @RequestHeader("X-Customer-Id") String customerId
    ) {
        GetMyPageDetailPointHistoriesResponse response = pointServiceImpl.getMyPageDetailPointHistories(Long.parseLong(customerId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/types")
    public ResponseEntity<List<PointTypeDto>> getActivePointTypes() {
        return ResponseEntity.ok(pointTypeServiceImpl.getAllActivePointTypes());
    }
}
