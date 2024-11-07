package com.nhnacademy.bookstore.point.service;

import com.nhnacademy.bookstore.point.dto.request.PointTypeDto;

import java.util.List;

public interface PointTypeService {

    PointTypeDto createPointType(PointTypeDto dto);
    PointTypeDto updatePointType(Long id, PointTypeDto dto);
    List<PointTypeDto> getAllActivePointTypes();
}
