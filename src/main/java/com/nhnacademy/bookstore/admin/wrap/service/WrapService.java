package com.nhnacademy.bookstore.admin.wrap.service;

import com.nhnacademy.bookstore.admin.wrap.dto.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;

import java.util.List;

public interface WrapService {
    void createWrap(WrapRequestDto requestDto);
    WrapResponseDto getWrap(Long WrapId);
    List<WrapResponseDto> getAllWraps();

    WrapResponseDto updateWrap(Long WrapId, WrapResponseDto);
}