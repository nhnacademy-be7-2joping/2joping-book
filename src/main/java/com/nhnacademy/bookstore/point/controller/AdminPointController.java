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

    @PutMapping("/types/{id}")
    public ResponseEntity<PointTypeDto> updatePointType(
            @PathVariable("id") Long id,
            @RequestBody @Valid PointTypeDto request
    ) {
        return ResponseEntity.ok(pointTypeServiceImpl.updatePointType(id, request));
    }
}
