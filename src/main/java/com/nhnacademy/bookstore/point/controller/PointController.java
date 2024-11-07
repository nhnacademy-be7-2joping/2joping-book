package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.request.PointHistoryDto;
import com.nhnacademy.bookstore.point.dto.request.PointTypeDto;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.service.impl.PointServiceImpl;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final PointServiceImpl pointServiceImpl;
    private final PointTypeServiceImpl pointTypeServiceImpl;

    @PostMapping("/use")
    public ResponseEntity<Void> usePoint(@RequestBody @Valid PointUseRequest request) {
        pointServiceImpl.usePoint(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<PointHistoryDto>> getPointHistory(
            @PathVariable Long customerId
    ) {
        return ResponseEntity.ok(pointServiceImpl.getPointHistory(customerId));
    }

    @GetMapping("/types")
    public ResponseEntity<List<PointTypeDto>> getActivePointTypes() {
        return ResponseEntity.ok(pointTypeServiceImpl.getAllActivePointTypes());
    }
}
