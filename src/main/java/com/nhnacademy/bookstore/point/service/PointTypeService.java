package com.nhnacademy.bookstore.point.service;

import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.request.UpdatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;

import java.util.List;

public interface PointTypeService {

    Long createPointType(CreatePointTypeRequestDto dto);

    UpdatePointTypeResponseDto updatePointType(Long id, UpdatePointTypeRequestDto dto);

    List<GetPointTypeResponse> getAllActivePointTypes();
}
