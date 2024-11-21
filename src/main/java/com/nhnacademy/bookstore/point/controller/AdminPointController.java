package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.response.PointTypeDto;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/points")
@RequiredArgsConstructor
public class AdminPointController {

    private final PointTypeServiceImpl pointTypeServiceImpl;

    @PostMapping("/types")
    public ResponseEntity<PointTypeDto> createPointType(@RequestBody @Valid PointTypeDto request) {
        return ResponseEntity.ok(pointTypeServiceImpl.createPointType(request));
    }

    @PutMapping("/types/{point-type-id}")
    public ResponseEntity<PointTypeDto> updatePointType(
            @PathVariable("point-type-id") Long pointTypeId,
            @RequestBody @Valid PointTypeDto request
    ) {
        return ResponseEntity.ok(pointTypeServiceImpl.updatePointType(pointTypeId, request));
    }
}
