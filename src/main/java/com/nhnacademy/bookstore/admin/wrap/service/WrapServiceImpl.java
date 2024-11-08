package com.nhnacademy.bookstore.admin.wrap.service;

import com.nhnacademy.bookstore.admin.wrap.dto.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.nhnacademy.bookstore.admin.wrap.repository.WrapRepository;
import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * wrap policy service
 *
 * @author : 박채연
 * @date : 2024-11-06
 */
@Service
@RequiredArgsConstructor

public class WrapServiceImpl implements WrapService {
    private final WrapRepository wrapRepository;

    @Override
    @Transactional
    public void createWrap(WrapRequestDto requestDto) {
        if (wrapRepository.findByName(requestDto.name()).isPresent()) {
            throw new ConflictException("이미 존재하는 포장상품 입니다.");
        }
        Wrap wrap = new Wrap(
                requestDto.name(),
                requestDto.wrapPrice(),
                requestDto.isActive()
        );

        wrapRepository.save(wrap);
    }


    @Override
    public WrapResponseDto getWrap(Long WrapId) {
        Wrap wrap = wrapRepository.findById(WrapId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 포장상품 입니다."));

        return new WrapResponseDto(
                wrap.getWrapId(),
                wrap.getName(),
                wrap.getWrapPrice(),
                wrap.isActive());
    }

    @Override
    public List<WrapResponseDto> getAllWraps() {
        List<Wrap> wraps = wrapRepository.findAll();
        return wraps.stream()
                .map(wrap -> new WrapResponseDto(
                        wrap.getWrapId(),
                        wrap.getName(),
                        wrap.getWrapPrice(),
                        wrap.isActive()))
                .collect(Collectors.toList()); // strema? 이거 써도되는지? querydsl?

    }

    @Override
    public WrapResponseDto updateWrap(Long WrapId, WrapResponseDto wrapResponseDto) {

        return null;
    }
}
