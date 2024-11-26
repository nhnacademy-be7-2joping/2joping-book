package com.nhnacademy.bookstore.bookset.contributor.controller;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorRoleService;
import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 도서 기여자 역할 Controller
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Tag(name = "ContributorRole", description = "도서 도서 기여자 역할 API")
@Validated
@RestController
@RequestMapping("/api/v1/bookstore/contributors/role")
@RequiredArgsConstructor
public class ContributorRoleController {
    private final ContributorRoleService contributorRoleService;

    /**
     * 새로운 도서 도서 기여자 역할을 생성합니다.
     *
     * @param dto 도서 기여자 역할 생성에 필요한 정보가 담긴 DTO
     * @return 생성된 도서 기여자 역할의 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "도서 기여자 역할 생성", description = "새로운 도서 도서 기여자 역할을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "도서 기여자 역할 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping
    public ResponseEntity<ContributorRoleResponseDto> createContributorRole(@RequestBody @Valid ContributorRoleRequestDto dto) {
        ContributorRoleResponseDto createdRole = contributorRoleService.createContributorRole(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    /**
     * 특정 도서 기여자 역할을 조회합니다.
     *
     * @param contributorRoleId 조회할 도서 기여자 역할의 ID
     * @return 조회된 도서 기여자 역할의 정보를 포함한 ResponseEntity
     * @throws ContributorRoleNotFoundException 도서 기여자 역할을 찾을 수 없는 경우 응답 코드 404 NOT_FOUND 반환합니다.
     */
    @Operation(summary = "도서 기여자 역할 조회", description = "특정 도서 기여자 역할을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "도서 기여자 역할 조회 성공")
    @ApiResponse(responseCode = "404", description = "도서 기여자 역할을 찾을 수 없음")
    @GetMapping("/{contributorRoleId}")
    public ResponseEntity<ContributorRoleResponseDto> getContributorRole(@PathVariable @ValidPathVariable Long contributorRoleId) {
        ContributorRoleResponseDto contributorRole = contributorRoleService.getContributorRole(contributorRoleId);
        return ResponseEntity.ok(contributorRole);
    }

    /**
     * 특정 도서 기여자 역할을 수정합니다.
     *
     * @param contributorRoleId 수정할 도서 기여자 역할의 ID
     * @param dto 수정할 도서 기여자 역할 정보가 담긴 DTO
     * @return 수정된 도서 기여자 역할의 정보를 포함한 ResponseEntity
     * @throws ContributorRoleNotFoundException 도서 기여자 역할을 찾을 수 없는 경우 응답 코드 404 NOT_FOUND 반환합니다.
     */
    @Operation(summary = "도서 기여자 역할 수정", description = "특정 도서 기여자 역할을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "도서 기여자 역할 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "404", description = "도서 기여자 역할을 찾을 수 없음")
    @PutMapping("/{contributorRoleId}")
    public ResponseEntity<ContributorRoleResponseDto> updateContributorRole(@PathVariable @ValidPathVariable Long contributorRoleId, @RequestBody @Valid ContributorRoleRequestDto dto) {
        ContributorRoleResponseDto updatedRole = contributorRoleService.updateContributorRole(contributorRoleId, dto);
        return ResponseEntity.ok(updatedRole);
    }

    /**
     * 특정 도서 기여자 역할을 삭제합니다.
     *
     * @param contributorRoleId 삭제할 도서 기여자 역할의 ID
     * @return HTTP 204 상태의 빈 ResponseEntity
     * @throws ContributorRoleNotFoundException 도서 기여자 역할을 찾을 수 없는 경우 응답 코드 404 NOT_FOUND 반환합니다.
     */
    @Operation(summary = "도서 기여자 역할 삭제", description = "특정 도서 기여자 역할을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "도서 기여자 역할 삭제 성공")
    @ApiResponse(responseCode = "404", description = "도서 기여자 역할을 찾을 수 없음")
    @DeleteMapping("/{contributorRoleId}")
    public ResponseEntity<Void> deleteContributorRole(@PathVariable @ValidPathVariable Long contributorRoleId) {
        contributorRoleService.deleteContributorRole(contributorRoleId);
        return ResponseEntity.noContent().build();
    }


    /**
     * 모든 도서 기여자 역할을 조회합니다.
     *
     * @return 모든 도서 기여자 역할의 리스트
     */
    @Operation(summary = "전체 도서 기여자 역할 리스트", description = "모든 도서 기여자 역할을 리스트로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "도서 기여자 역할 리스트 조회 성공")
    @GetMapping
    public ResponseEntity<List<ContributorRoleResponseDto>> getAllContributorRoles() {
        List<ContributorRoleResponseDto> roles = contributorRoleService.getAllContributorRoles();
        return ResponseEntity.ok(roles);
    }
}
