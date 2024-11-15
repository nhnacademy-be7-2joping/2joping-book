package com.nhnacademy.bookstore.admin.wrap.controller;

import com.nhnacademy.bookstore.admin.wrap.dto.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.service.WrapService;
import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 포장 Controller
 *
 * @author : 박채연
 * @date : 2024-11-05
 */
@Tag(name = "Wrap", description = "포장 API")
@RestController
@RequestMapping("/api/v1/wraps")
@RequiredArgsConstructor
public class WrapController {


    private final WrapService wrapService;

    /**
     * 포장상품 생성
     *
     * 포장상품을 새로 생성합니다.
     *
     * @param requestDto 포장상품 생성 요청 데이터
     * @return 201 Created 상태
     */
    @Operation(summary = "포장상품 생성", description = "새로운 포장상품을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "포장상품 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<Void> createWrap(@Valid @RequestBody WrapRequestDto requestDto) {
        wrapService.createWrap(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 특정 포장상품 조회
     *
     * @param wrapId 조회할 포장상품의 ID
     * @return 포장상품의 상세 정보
     */
    @Operation(summary = "포장상품 조회", description = "특정 포장상품의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포장상품 조회 성공"),
            @ApiResponse(responseCode = "404", description = "포장상품을 찾을 수 없음")
    })
    @GetMapping("/{wrap-id}")
    public ResponseEntity<WrapResponseDto> getWrap(@PathVariable("wrap-id") @ValidPathVariable Long wrapId) {
        WrapResponseDto wrap = wrapService.getWrap(wrapId);
        return ResponseEntity.ok(wrap);
    }

    /**
     * 활성화 된 포장상품 목록 조회
     *
     * 활성화 된 포장상품을 조회합니다.
     *
     * @return 포장상품 목록
     */
    @Operation(summary = "활성화 된 포장상품 목록 조회", description = "활성화 된 포장상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "포장상품 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<WrapResponseDto>> findAllByIsActiveTrue() {
        List<WrapResponseDto> wrap = wrapService.findAllByIsActiveTrue();
        return ResponseEntity.ok(wrap);
    }

    /**
     * 포장상품 수정
     *
     * 특정 포장상품을 수정합니다.
     *
     * @param wrapId 수정할 포장상품의 ID
     * @return 수정된 포장상품 정보
     */
    @Operation(summary = "포장상품 수정", description = "특정 포장상품을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포장상품 수정 성공"),
            @ApiResponse(responseCode = "404", description = "포장상품을 찾을 수 없음")
    })
    @PutMapping("/{wrap-id}")
    public ResponseEntity<WrapResponseDto> updateWrap(
            @PathVariable("wrap-id") @Positive Long wrapId,
            @RequestBody WrapRequestDto dto) {

        WrapResponseDto updatedWrap = wrapService.updateWrap(wrapId, dto);
        return ResponseEntity.ok(updatedWrap);
    }


//    /**
//     * 포장상품 삭제
//     *
//     * 특정 포장상품을 삭제합니다.
//     *
//     * @param wrapId 삭제할 포장상품의 ID
//     * @return 상태 204 (No Content)
//     */
//    @Operation(summary = "포장상품 삭제", description = "특정 포장상품을 삭제합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "포장상품 삭제 후 목록 반환 성공"),
//            @ApiResponse(responseCode = "404", description = "포장상품을 찾을 수 없음")
//    })
//
//    @DeleteMapping("/{wrap-id}")
//    public ResponseEntity<List<WrapResponseDto>> deleteWrap(@PathVariable("wrap-id") Long wrapId) {
//        wrapService.deleteWrap(wrapId);
//        List<WrapResponseDto> wrapList = wrapService.getAllWraps();
//        return ResponseEntity.ok(wrapList);
//    }
}