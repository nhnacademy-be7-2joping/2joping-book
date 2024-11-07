package com.nhnacademy.bookstore.admin.wrappolicy.service;

import com.nhnacademy.bookstore.admin.wrappolicy.dto.WrapPolicyRequestDto;
import com.nhnacademy.bookstore.admin.wrappolicy.dto.WrapPolicyRequestDto;
import com.nhnacademy.bookstore.admin.wrappolicy.dto.WrapPolicyResponseDto;
import com.nhnacademy.bookstore.admin.wrappolicy.dto.WrapPolicyResponseDto;
import com.nhnacademy.bookstore.admin.wrappolicy.entity.WrapPolicy;
import com.nhnacademy.bookstore.admin.wrappolicy.repository.WrapPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * wrap policy service
 *
 * @author : 박채연
 * @date : 2024-11-06
 */
@Service
@RequiredArgsConstructor

public class WrapPolicyServiceImpl implements WrapPolicyService {
    private final WrapPolicyRepository wrapPolicyRepository;

    @Override
    @Transactional
    public WrapPolicyResponseDto createWrapPolicy(WrapPolicyRequestDto requestDto) {
        WrapPolicy wrapPolicy = new WrapPolicy(
                requestDto.name(),
                requestDto.wrapPrice(),
                requestDto.isActive()
        );

        WrapPolicy savedPolicy = wrapPolicyRepository.save(wrapPolicy);

        return new WrapPolicyResponseDto(
                savedPolicy.getWrapId(),
                savedPolicy.getName(),
                savedPolicy.getWrapPrice(),
                savedPolicy.isActive()
        );

    }
//
//    @Override
//    @Transactional
//    public WrapPolicyResponseDto getWrapPolicy(Long WrapPolicyId) {
//
//    }
}
