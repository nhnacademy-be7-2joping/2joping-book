package com.nhnacademy.bookstore.admin.wrap.service;

import com.nhnacademy.bookstore.admin.wrap.dto.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.nhnacademy.bookstore.admin.wrap.repository.WrapRepository;
import com.nhnacademy.bookstore.common.error.exception.wrap.WrapAlreadyExistException;
import com.nhnacademy.bookstore.common.error.exception.wrap.WrapNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



/**
 * 포장 상품 서비스 구현
 * 포장 상품 생성, 조회, 수정, 삭제 기능을 제공합니다.
 * <p>
 * 작성자: 박채연
 * 작성일: 2024-11-06
 */
@Service
@RequiredArgsConstructor
public class WrapServiceImpl implements WrapService {
    private final WrapRepository wrapRepository;

    /**
     * 새로운 포장 상품을 생성합니다.
     *
     * @param requestDto 포장 상품 요청 DTO
     * @throws WrapAlreadyExistException 동일한 이름의 포장 상품이 이미 존재하는 경우
     */
    @Override
    @Transactional
    public void createWrap(WrapRequestDto requestDto) {
        if (wrapRepository.findByName(requestDto.name()).isPresent()) {
            throw new WrapAlreadyExistException();
        }
        Wrap wrap = new Wrap(
                requestDto.name(),
                requestDto.wrapPrice(),
                requestDto.isActive()
        );

        wrapRepository.save(wrap);
    }

    /**
     * 포장 상품을 ID로 조회합니다.
     *
     * @param wrapId 포장 상품의 ID
     * @return 포장 상품 응답 DTO
     * @throws WrapNotFoundException 포장 상품을 찾을 수 없는 경우
     */
    @Override
    public WrapResponseDto getWrap(Long wrapId) {
        Wrap wrap = wrapRepository.findById(wrapId)
                .orElseThrow(WrapNotFoundException::new);

        return new WrapResponseDto(
                wrap.getWrapId(),
                wrap.getName(),
                wrap.getWrapPrice(),
                wrap.isActive());
    }

    /**
     * 활성화 된 포장 상품을 조회합니다.
     *
     * @return 포장 상품 응답 DTO 리스트
     */
    @Override // query dsl 사용
    public List<WrapResponseDto> findAllByIsActiveTrue() {
        return wrapRepository.findAllByIsActiveTrue();
    }


    /**
     * 포장 상품을 업데이트합니다.
     *
     * @param wrapId 업데이트할 포장 상품의 ID
     * @param dto    업데이트할 포장 상품 데이터
     * @return 업데이트된 포장 상품 응답 DTO
     * @throws WrapNotFoundException 포장 상품을 찾을 수 없는 경우
     */
    @Override
    public WrapResponseDto updateWrap(Long wrapId, WrapRequestDto dto) {
        Wrap wrap = wrapRepository.findById(wrapId)
                .orElseThrow(WrapNotFoundException::new);

        wrap.updateWrap(
                dto.name(),
                dto.wrapPrice(),
                dto.isActive()
        );

        Wrap updatedWrap = wrapRepository.save(wrap);
        return new WrapResponseDto(updatedWrap.getWrapId(), updatedWrap.getName(), updatedWrap.getWrapPrice(), updatedWrap.isActive());
    }

//    /**
//     * 포장 상품을 ID로 삭제합니다.
//     *
//     * @param wrapId 삭제할 포장 상품의 ID
//     * @throws WrapNotFoundException 포장 상품을 찾을 수 없는 경우
//     */
//    @Override
//    public void deleteWrap(Long wrapId) {
//        wrapRepository.findById(wrapId)
//                .orElseThrow(WrapNotFoundException::new);
//        wrapRepository.deleteById(wrapId);
//    }
}


