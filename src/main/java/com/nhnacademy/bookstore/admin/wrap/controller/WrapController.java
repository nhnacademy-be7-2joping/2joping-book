package com.nhnacademy.bookstore.admin.wrap.controller;

import com.nhnacademy.bookstore.admin.wrap.dto.request.WrapRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.request.WrapUpdateRequestDto;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapCreateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapUpdateResponseDto;
import com.nhnacademy.bookstore.admin.wrap.service.WrapService;
import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * <p>
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
    @ResponseStatus(HttpStatus.CREATED) // 테스트코드 createwrap 때문에 추가
    public WrapCreateResponseDto createWrap(@Valid @RequestBody WrapRequestDto requestDto) {
        return wrapService.createWrap(requestDto);
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
    public WrapUpdateResponseDto getWrap(@PathVariable("wrap-id") @ValidPathVariable Long wrapId) {
        return wrapService.getWrap(wrapId);
    }

    /**
     * 활성화 된 포장상품 목록 조회
     * <p>
     * 활성화 된 포장상품을 조회합니다.
     *
     * @return 포장상품 목록
     */
    @Operation(summary = "활성화 된 포장상품 목록 조회", description = "활성화 된 포장상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "포장상품 목록 조회 성공")
    @GetMapping
    public List<WrapUpdateResponseDto> findAllByIsActiveTrue() {
        return wrapService.findAllByIsActiveTrue();
    }

    /**
     * 포장상품 수정
     * <p>
     * 특정 포장상품을 수정합니다.
     *
     * @param wrapId 수정할 포장상품의 ID
     * @return 수정된 포장상품 정보
     */
    @Operation(summary = "포장상품 수정", description = "특정 포장상품을 수정합니다.")
    @PutMapping("/{wrap-id}")
    public ResponseEntity<WrapUpdateResponseDto> updateWrap(@PathVariable("wrap-id") @Positive Long wrapId,
                                                            @Valid @RequestBody WrapUpdateRequestDto wrapUpdateRequestDto) {

        WrapUpdateResponseDto wrapUpdateResponseDto = wrapService.updateWrap(wrapId, wrapUpdateRequestDto);
        return ResponseEntity.ok(wrapUpdateResponseDto);
    }


}