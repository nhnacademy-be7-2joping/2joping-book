package com.nhnacademy.bookstore.admin.wrappolicy.service;

import com.nhnacademy.bookstore.admin.wrappolicy.dto.WrapPolicyRequestDto;
import com.nhnacademy.bookstore.admin.wrappolicy.dto.WrapPolicyResponseDto;

public interface WrapPolicyService {
    WrapPolicyResponseDto createWrapPolicy(WrapPolicyRequestDto requestDto);
}