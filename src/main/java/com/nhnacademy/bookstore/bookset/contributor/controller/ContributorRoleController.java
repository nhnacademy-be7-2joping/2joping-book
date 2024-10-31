package com.nhnacademy.bookstore.bookset.contributor.controller;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 도서 기여자 역할 Controller
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Tag(name = "ContributorRole", description = "도서 기여자 역할 API")
@Validated
@RestController
@RequestMapping("/bookstore/contributors/role")
@RequiredArgsConstructor
public class ContributorRoleController {
    private final ContributorRoleService contributorRoleService;

    @Operation(summary = "Create a new contributor role", description = "새로운 도서 기여자 역할을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "기여자 역할 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
    })
    @PostMapping
    public ResponseEntity<ContributorRoleResponseDto> createContributorRole(@RequestBody @Valid ContributorRoleRequestDto dto) {
        ContributorRoleResponseDto createdRole = contributorRoleService.createContributorRole(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @Operation(summary = "Get a contributor role", description = "특정 기여자 역할을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기여자 역할 조회 성공"),
            @ApiResponse(responseCode = "404", description = "기여자 역할을 찾을 수 없음"),
    })
    @GetMapping("/{contributorRoleId}")
    public ResponseEntity<ContributorRoleResponseDto> getContributorRole(@PathVariable Long contributorRoleId) {
        ContributorRoleResponseDto contributorRole = contributorRoleService.getContributorRole(contributorRoleId);
        return ResponseEntity.ok(contributorRole);
    }

    @Operation(summary = "Update a contributor role", description = "특정 기여자 역할을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기여자 역할 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "기여자 역할을 찾을 수 없음"),
    })
    @PutMapping("/{contributorRoleId}")
    public ResponseEntity<ContributorRoleResponseDto> updateContributorRole(@PathVariable Long contributorRoleId, @RequestBody @Valid ContributorRoleRequestDto dto) {
        ContributorRoleResponseDto updatedRole = contributorRoleService.updateContributorRole(contributorRoleId, dto);
        return ResponseEntity.ok(updatedRole);
    }

    @Operation(summary = "Delete a contributor role", description = "특정 기여자 역할을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "기여자 역할 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "기여자 역할을 찾을 수 없음"),
    })
    @DeleteMapping("/{contributorRoleId}")
    public ResponseEntity<Void> deleteContributorRole(@PathVariable Long contributorRoleId) {
        contributorRoleService.deleteContributorRole(contributorRoleId);
        return ResponseEntity.noContent().build();
    }
}
