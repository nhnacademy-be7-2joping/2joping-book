package com.nhnacademy.bookstore.admin.wrap.service;

import com.nhnacademy.bookstore.admin.wrap.dto.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.nhnacademy.bookstore.admin.wrap.repository.WrapRepository;
import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 포장 정책 서비스 구현
 * 포장 정책 생성, 조회, 수정, 삭제 기능을 제공합니다.
 *
 * 작성자: 박채연
 * 작성일: 2024-11-06
 */
@Service
@RequiredArgsConstructor
public class WrapServiceImpl implements WrapService {
    private final WrapRepository wrapRepository;

    /**
     * 새로운 포장 정책을 생성합니다.
     *
     * @param requestDto 포장 정책 요청 DTO
     * @throws ConflictException 동일한 이름의 포장 상품이 이미 존재하는 경우
     */
    @Override
    @Transactional
    @Operation(summary = "새로운 포장 정책 생성", description = "새로운 포장 정책을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "포장 정책 생성 성공"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 포장 상품입니다.")
    })
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

    /**
     * 포장 정책을 ID로 조회합니다.
     *
     * @param wrapId 포장 정책의 ID
     * @return 포장 정책 응답 DTO
     * @throws NotFoundException 포장 정책을 찾을 수 없는 경우
     */
    @Override
    @Operation(summary = "포장 정책 조회", description = "포장 정책의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포장 정책 조회 성공"),
            @ApiResponse(responseCode = "404", description = "포장 정책을 찾을 수 없음")
    })
    public WrapResponseDto getWrap(Long wrapId) {
        Wrap wrap = wrapRepository.findById(wrapId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 포장상품 입니다."));

        return new WrapResponseDto(
                wrap.getWrapId(),
                wrap.getName(),
                wrap.getWrapPrice(),
                wrap.isActive());
    }

    /**
     * 모든 포장 정책을 조회합니다.
     *
     * @return 포장 정책 응답 DTO 리스트
     */
    @Override
    @Operation(summary = "모든 포장 정책 조회", description = "모든 포장 정책을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "포장 정책 목록 조회 성공")
    public List<WrapResponseDto> getAllWraps() {
        List<Wrap> wraps = wrapRepository.findAll();
        List<WrapResponseDto> responseList = new ArrayList<>();

        for (Wrap wrap : wraps) {
            responseList.add(new WrapResponseDto(
                    wrap.getWrapId(),
                    wrap.getName(),
                    wrap.getWrapPrice(),
                    wrap.isActive()
            ));
        }

        return responseList;
    }


    /**
     * 포장 정책을 업데이트합니다.
     *
     * @param wrapId 업데이트할 포장 정책의 ID
     * @param dto    업데이트할 포장 정책 데이터
     * @return 업데이트된 포장 정책 응답 DTO
     * @throws NotFoundException 포장 정책을 찾을 수 없는 경우
     */
    @Override
    @Operation(summary = "포장 정책 업데이트", description = "포장 정책을 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포장 정책 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "포장 정책을 찾을 수 없음")
    })
    public WrapResponseDto updateWrap(Long wrapId, WrapRequestDto dto) {
        Wrap wrap = wrapRepository.findById(wrapId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 포장상품입니다."));

        wrap.updateWrap(
                dto.name(),
                dto.wrapPrice(),
                dto.isActive()
        );

        Wrap updatedWrap = wrapRepository.save(wrap);
        return new WrapResponseDto(updatedWrap.getWrapId(), updatedWrap.getName(), updatedWrap.getWrapPrice(), updatedWrap.isActive());
    }

    /**
     * 포장 정책을 ID로 삭제합니다.
     *
     * @param wrapId 삭제할 포장 정책의 ID
     * @throws NotFoundException 포장 정책을 찾을 수 없는 경우
     */
    @Override
    @Operation(summary = "포장 정책 삭제", description = "포장 정책을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "포장 정책 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "포장 정책을 찾을 수 없음")
    })
    public void deleteWrap(Long wrapId) {
        wrapRepository.findById(wrapId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 포장상품입니다."));
        wrapRepository.deleteById(wrapId);
    }
}
