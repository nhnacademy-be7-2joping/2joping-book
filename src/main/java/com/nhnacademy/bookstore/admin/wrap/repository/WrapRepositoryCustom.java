package com.nhnacademy.bookstore.admin.wrap.repository;

import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapResponseDto;

import java.util.List;

public interface WrapRepositoryCustom {
    List<WrapResponseDto> findAllByIsActiveTrue();
}
