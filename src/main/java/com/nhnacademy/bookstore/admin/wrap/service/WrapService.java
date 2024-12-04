package com.nhnacademy.bookstore.admin.wrap.service;

import com.nhnacademy.bookstore.admin.wrap.dto.request.WrapModifyRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.request.WrapUpdateRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapCreateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.dto.request.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapUpdateResponseDto;

import java.util.List;

public interface WrapService {
    WrapCreateResponseDto createWrap(WrapRequestDto requestDto);
    WrapUpdateResponseDto getWrap(Long WrapId);
    List<WrapUpdateResponseDto> findAllByIsActiveTrue();
    WrapUpdateResponseDto updateWrap(Long WrapId, WrapUpdateRequestDto wrapUpdateRequestDto);
    void deleteWrap(Long wrapId);
}