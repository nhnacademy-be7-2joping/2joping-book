package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.request.PointHistoryDto;
import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.service.impl.PointServiceImpl;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

    @PostMapping("/uses")
    public ResponseEntity<Void> usePoint(@RequestBody @Valid PointUseRequest request) {
        pointServiceImpl.usePoint(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/histories/{customerId}")
    public ResponseEntity<List<PointHistoryDto>> getPointHistory(@Positive @PathVariable Long customerId) {
        return ResponseEntity.ok(pointServiceImpl.getPointHistory(customerId));
    }

    @GetMapping("/types")
    public ResponseEntity<List<ReadPointTypeResponseDto>> getActivePointTypes() {
        return ResponseEntity.ok(pointTypeServiceImpl.getAllActivePointTypes());
    }
}
