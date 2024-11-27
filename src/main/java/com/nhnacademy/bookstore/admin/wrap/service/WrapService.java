package com.nhnacademy.bookstore.admin.wrap.service;

import com.nhnacademy.bookstore.admin.wrap.dto.WrapCreateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;

import java.util.List;

public interface WrapService {
    WrapCreateResponseDto createWrap(WrapRequestDto requestDto);
    WrapResponseDto getWrap(Long WrapId);
    List<WrapResponseDto> findAllByIsActiveTrue();

    //WrapResponseDto updateWrap(Long WrapId, WrapRequestDto wrapRequestDto);

//    void deleteWrap(Long wrapId);
}