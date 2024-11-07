package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.request.PointHistoryDto;
import com.nhnacademy.bookstore.point.dto.request.PointTypeDto;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.service.PointService;
import com.nhnacademy.bookstore.point.service.PointTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;
    private final PointTypeService pointTypeService;

    @PostMapping("/use")
    public ResponseEntity<Void> usePoint(@RequestBody @Valid PointUseRequest request) {
        pointService.usePoint(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<PointHistoryDto>> getPointHistory(
            @PathVariable Long customerId
    ) {
        return ResponseEntity.ok(pointService.getPointHistory(customerId));
    }

    @GetMapping("/types")
    public ResponseEntity<List<PointTypeDto>> getActivePointTypes() {
        return ResponseEntity.ok(pointTypeService.getAllActivePointTypes());
    }
}
