package com.nhnacademy.bookstore.point.repository;

import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;

import java.util.List;

public interface PointTypeRepositoryCustom {
    List<ReadPointTypeResponseDto> findAllActivePointTypes();
}
