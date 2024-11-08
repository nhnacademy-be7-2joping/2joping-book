package com.nhnacademy.bookstore.admin.wrap.controller;

import com.nhnacademy.bookstore.admin.wrap.dto.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.service.WrapService;
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
 * 포장정책 Controller
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
     * 포장 정책을 생성하는 컨트롤러입니다.
     *
     * @param requestDto 포장 정책 생성 요청 데이터
     * @return 생성된 포장 정책 정보
     */
    @Operation(summary = "Create a new wrap policy", description = "새로운 포장 정책을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "포장 정책 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
    })

    @PostMapping
    public ResponseEntity<Void> createWrapPolicy(@Valid @RequestBody WrapRequestDto requestDto) {
        wrapService.createWrap(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 특정 포장 정책을 조회하는 컨트롤러입니다.
     *
     * @param wrapId 조회할 포장 정책의 ID
     * @return 조회된 포장 정책 정보
     */
    @Operation(summary = "Get wrap policy details", description = "포장 정책의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포장 정책 조회 성공"),
            @ApiResponse(responseCode = "404", description = "포장 정책을 찾을 수 없음")
    })

    @GetMapping("/{wrap-id}")
    public ResponseEntity<WrapResponseDto> getWrap(@PathVariable("wrap-id") @Positive Long wrapId) {
        WrapResponseDto wrap = wrapService.getWrap(wrapId);
        return ResponseEntity.ok(wrap);
    }

    @GetMapping("/list")
    public ResponseEntity<List<WrapResponseDto>> getAllWraps() {
        List<WrapResponseDto> wrap = wrapService.getAllWraps();
        return  ResponseEntity.ok(wrap);
    }
}
