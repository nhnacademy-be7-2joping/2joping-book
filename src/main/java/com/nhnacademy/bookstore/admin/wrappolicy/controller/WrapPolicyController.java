package com.nhnacademy.bookstore.admin.wrappolicy.controller;

import com.nhnacademy.bookstore.admin.wrappolicy.dto.WrapPolicyRequestDto;
import com.nhnacademy.bookstore.admin.wrappolicy.dto.WrapPolicyResponseDto;
import com.nhnacademy.bookstore.admin.wrappolicy.service.WrapPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * 포장정책 Controller
 *
 * @author : 박채연
 * @date : 2024-11-05
 */
@Tag(name = "WrapPolicy", description = "포장정책 API")
@RestController
@RequestMapping("/api/v1/wrappolicy")
@RequiredArgsConstructor
public class WrapPolicyController {

    private final WrapPolicyService wrapPolicyService;

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
    public ResponseEntity<WrapPolicyResponseDto> createWrapPolicy(@Valid @RequestBody WrapPolicyRequestDto requestDto) {
        WrapPolicyResponseDto createdPolicy = wrapPolicyService.createWrapPolicy(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPolicy); // body를 반환해야 하는지 생각해보기
        // 똑같은 포장정책 이름이 있어도 되는지 생각해보기
    }
}
