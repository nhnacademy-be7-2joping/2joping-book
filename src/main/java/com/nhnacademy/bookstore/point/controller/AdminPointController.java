package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.request.PointTypeDto;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/points")
public class AdminPointController {

    private PointTypeServiceImpl pointTypeServiceImpl;

    @PostMapping("/types")
    public ResponseEntity<PointTypeDto> createPointType(@RequestBody @Valid PointTypeDto request) {
        return ResponseEntity.ok(pointTypeServiceImpl.createPointType(request));
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<PointTypeDto> updatePointType(
            @PathVariable Long id,
            @RequestBody @Valid PointTypeDto request
    ) {
        return ResponseEntity.ok(pointTypeServiceImpl.updatePointType(id, request));
    }
}
