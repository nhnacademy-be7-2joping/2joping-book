package com.nhnacademy.bookstore.bookset.contributor.controller;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorNameRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorService;
import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorIsDeactivateException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 도서 기여자 Controller
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Tag(name = "Contributor", description = "도서 기여자 API")
@Validated
@RestController
@RequestMapping("/api/v1/bookstore/contributors")
@RequiredArgsConstructor
public class ContributorController {
    private final ContributorService contributorService;

    /**
     * 새로운 도서 기여자를 생성합니다.
     *
     * @param dto 도서 기여자 생성에 필요한 정보가 담긴 DTO
     * @return 생성된 도서 기여자의 정보를 포함한 ResponseEntity
     */
    @Operation(summary = "도서 기여자 생성", description = "새로운 도서 기여자를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "도서 기여자 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping
    public ResponseEntity<ContributorResponseDto> createContributor(@RequestBody @Valid ContributorRequestDto dto) {
        ContributorResponseDto createdContributor = contributorService.createContributor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContributor);
    }

    /**
     * 특정 도서 기여자의 정보를 조회합니다.
     *
     * @param contributorId 조회할 도서 기여자의 ID
     * @return 조회된 도서 기여자의 정보를 포함한 ResponseEntity
     * @throws ContributorNotFoundException 도서 기여자를 찾을 수 없는 경우 응답 코드 404 NOT_FOUND 반환합니다.
     * @throws ContributorIsDeactivateException 비활성화된 도서 기여자를 조회하려 할 때 응답 코드 400 BAD_REQUEST 반환합니다.
     */
    @Operation(summary = "도서 기여자 조회", description = "특정 도서 기여자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "도서 기여자 조회 성공")
    @ApiResponse(responseCode = "404", description = "도서 기여자를 찾을 수 없음")
    @GetMapping("/{contributorId}")
    public ResponseEntity<ContributorResponseDto> getContributor(@PathVariable @ValidPathVariable Long contributorId) {
        ContributorResponseDto contributor = contributorService.getContributor(contributorId);
        return ResponseEntity.ok(contributor);
    }

    /**
     * 특정 도서 기여자의 정보를 수정합니다.
     *
     * @param contributorId 수정할 도서 기여자의 ID
     * @param dto 수정할 도서 기여자 정보가 담긴 DTO
     * @return 수정된 도서 기여자의 정보를 포함한 ResponseEntity
     * @throws ContributorNotFoundException 도서 기여자를 찾을 수 없는 경우 응답 코드 404 NOT_FOUND 반환합니다.
     * @throws ContributorIsDeactivateException 비활성화된 도서 기여자를 수정하려 할 때 응답 코드 400 BAD_REQUEST 반환합니다.
     */
    @Operation(summary = "도서 기여자 수정", description = "특정 도서 기여자의 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "도서 기여자 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "404", description = "도서 기여자를 찾을 수 없음")
    @PutMapping("/{contributorId}")
    public ResponseEntity<ContributorResponseDto> updateContributor(@PathVariable @ValidPathVariable Long contributorId, @RequestBody @Valid ContributorRequestDto dto) {
        ContributorResponseDto updatedContributor = contributorService.updateContributor(contributorId, dto);
        return ResponseEntity.ok(updatedContributor);
    }

    /**
     * 특정 도서 기여자를 비활성화합니다.
     *
     * @param contributorId 비활성화할 도서 기여자의 ID
     * @return HTTP 200 상태의 빈 ResponseEntity
     * @throws ContributorNotFoundException 도서 기여자를 찾을 수 없는 경우 응답 코드 404 NOT_FOUND 반환합니다.
     */
    @Operation(summary = "도서 기여자 비활성화", description = "특정 도서 기여자를 비활성화(약삭제)합니다.")
    @ApiResponse(responseCode = "200", description = "도서 기여자 비활성화 성공")
    @ApiResponse(responseCode = "404", description = "도서 기여자를 찾을 수 없음")
    @PutMapping("/{contributorId}/deactivate")
    public ResponseEntity<Void> deactivateContributor(@PathVariable @ValidPathVariable Long contributorId) {
        contributorService.deactivateContributor(contributorId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 도서 기여자를 활성화합니다.
     *
     * @param contributorId 활성화할 도서 기여자의 ID
     * @return HTTP 200 상태의 빈 ResponseEntity
     * @throws ContributorNotFoundException 도서 기여자를 찾을 수 없는 경우 응답 코드 404 NOT_FOUND 반환합니다.
     */
    @Operation(summary = "도서 기여자 활성화", description = "특정 도서 기여자를 활성화 합니다.")
    @ApiResponse(responseCode = "200", description = "도서 기여자 활성화 성공")
    @ApiResponse(responseCode = "404", description = "도서 기여자를 찾을 수 없음")
    @PutMapping("/{contributorId}/activate")
    public ResponseEntity<Void> activateContributor(@PathVariable @ValidPathVariable Long contributorId) {
        contributorService.activateContributor(contributorId);
        return ResponseEntity.ok().build();
    }

    /**
     * 기여자명과 역할 리스트를 반환합니다.
     *
     * @return HTTP 200 상태의 빈 ResponseEntity
     */
    @Operation(summary = "도서 기여자 이름, 해당 기여자 역할 이름 리스트 조회", description = "도서 기여자의 이름과 역할 리스트를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "도서 기여자의 이름 및 역할 리스트 반환 성공")
    @ApiResponse(responseCode = "404", description = "도서 기여자를 찾을 수 없음")
    @GetMapping("/active")
    public List<ContributorNameRoleResponseDto> getActiveContributors() {
        return contributorService.getActiveContributorsWithRoles();
    }


    /**
     * 모든 도서 기여자를 조회합니다 (페이지 형식).
     *
     * @param pageable 페이징 정보를 담은 객체
     * @return 페이지 형식의 도서 기여자 리스트
     */
    @Operation(summary = "도서 기여자 조회 (페이징)", description = "모든 도서 기여자를 페이지 형식으로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "도서 기여자 페이지 조회 성공")
    @GetMapping
    public ResponseEntity<Page<ContributorIsActiveResponseDto>> getAllContributors(@PageableDefault(size = 20) Pageable pageable) {
        Page<ContributorIsActiveResponseDto> contributors = contributorService.getAllContributors(pageable);
        return ResponseEntity.ok(contributors);
    }

}
