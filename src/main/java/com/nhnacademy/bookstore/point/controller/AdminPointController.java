package com.nhnacademy.bookstore.point.controller;

import com.nhnacademy.bookstore.point.dto.request.CreatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.request.UpdatePointTypeRequestDto;
import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.dto.response.UpdatePointTypeResponseDto;
import com.nhnacademy.bookstore.point.service.impl.PointTypeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Admin Point Controller
 * <p>
 * 포인트 타입 관리 API를 제공하는 컨트롤러로, 포인트 타입 생성, 수정, 활성화된 포인트 타입 목록 조회 기능을 포함합니다.
 *
 * @author : 박채연
 * @date : 2024-11-18
 **/
@Tag(name = "Admin Point", description = "포인트 타입 관리 API")
@RestController
@RequestMapping("/api/v1/admin/pointtypes")
public class AdminPointController {

    private final PointTypeServiceImpl pointTypeServiceImpl;

    public AdminPointController(PointTypeServiceImpl pointTypeServiceImpl) {
        this.pointTypeServiceImpl = pointTypeServiceImpl;
    }

    /**
     * 포인트 타입 생성
     * <p>
     * 새로운 포인트 타입을 생성합니다.
     * </p>
     *
     * @param request 포인트 타입 생성 요청 데이터
     * @return 생성된 포인트 타입 정보와 HTTP 200 OK 상태 코드
     */
    @Operation(summary = "포인트 타입 생성", description = "새로운 포인트 타입을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "포인트 타입 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping
    public ResponseEntity<Void> createPointType(
            @RequestBody @Valid CreatePointTypeRequestDto request
    ) {
        Long pointTypeId = pointTypeServiceImpl.createPointType(request);
        return ResponseEntity.created(URI.create("/api/v1/admin/pointtypes/" + pointTypeId)).build();
    }

    /**
     * 포인트 타입 수정
     * <p>
     * 특정 포인트 타입의 정보를 수정합니다.
     * </p>
     *
     * @param typeId  수정할 포인트 타입의 ID
     * @param request 포인트 타입 수정 요청 데이터
     * @return 수정된 포인트 타입 정보와 HTTP 200 OK 상태 코드
     */
    @Operation(summary = "포인트 타입 수정", description = "특정 포인트 타입의 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "포인트 타입 수정 성공")
    @ApiResponse(responseCode = "404", description = "포인트 타입을 찾을 수 없음")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PutMapping("/{type-id}")
    public ResponseEntity<UpdatePointTypeResponseDto> updatePointType(
            @PathVariable("type-id") Long typeId,
            @RequestBody @Valid UpdatePointTypeRequestDto request) {
        UpdatePointTypeResponseDto responseDto = pointTypeServiceImpl.updatePointType(typeId, request);
        return ResponseEntity.ok(responseDto);
    }


    /**
     * 활성화된 포인트 타입 목록 조회
     * <p>
     * 활성화된 모든 포인트 타입 목록을 조회합니다.
     * </p>
     *
     * @return 활성화된 포인트 타입 목록과 HTTP 200 OK 상태 코드
     */
    @Operation(summary = "활성화된 포인트 타입 목록 조회", description = "활성화된 모든 포인트 타입 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "포인트 타입 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<GetPointTypeResponse>> readPointType() {
        List<GetPointTypeResponse> activePointTypes = pointTypeServiceImpl.getAllActivePointTypes();
        return ResponseEntity.ok(activePointTypes);
    }

    /**
     * 특정 포인트 타입 조회
     * 지정된 ID의 포인트 타입을 조회합니다.
     *
     * @param typeId 조회할 포인트 타입의 ID
     * @return 포인트 타입 정보와 HTTP 200 OK 상태 코드
     */
    @Operation(summary = "특정 포인트 타입 조회", description = "지정된 ID의 포인트 타입을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "포인트 타입 조회 성공")
    @ApiResponse(responseCode = "404", description = "포인트 타입을 찾을 수 없음")
    @GetMapping("/{type-id}")
    public ResponseEntity<ReadPointTypeResponseDto> getPointTypeById(@PathVariable("type-id") Long typeId) {
        ReadPointTypeResponseDto pointType = pointTypeServiceImpl.getPointTypeById(typeId);
        return ResponseEntity.ok(pointType);
    }
}
