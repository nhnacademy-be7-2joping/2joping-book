package com.nhnacademy.bookstore.bookset.contributor.controller;

import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRequestDto;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.service.ContributorService;
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
 * 도서 기여자 Controller
 *
 * @author : 양준하
 * @date : 2024-10-24
 */

@Tag(name = "Contributor", description = "도서 기여자 API")
@Validated
@RestController
@RequestMapping("/bookstore/contributors")
@RequiredArgsConstructor
public class ContributorController {
    private final ContributorService contributorService;

    @Operation(summary = "Create a new contributor", description = "새로운 도서 기여자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "기여자 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
    })
    @PostMapping
    public ResponseEntity<ContributorResponseDto> createContributor(@RequestBody @Valid ContributorRequestDto dto) {
        ContributorResponseDto createdContributor = contributorService.createContributor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContributor);
    }

    @Operation(summary = "Get a contributor", description = "특정 기여자의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기여자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "기여자를 찾을 수 없음"),
    })
    @GetMapping("/{contributorId}")
    public ResponseEntity<ContributorResponseDto> getContributor(@PathVariable Long contributorId) {
        ContributorResponseDto contributor = contributorService.getContributor(contributorId);
        return ResponseEntity.ok(contributor);
    }

    @Operation(summary = "Update a contributor", description = "특정 기여자의 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기여자 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "기여자를 찾을 수 없음"),
    })
    @PutMapping("/{contributorId}")
    public ResponseEntity<ContributorResponseDto> updateContributor(@PathVariable Long contributorId, @RequestBody @Valid ContributorRequestDto dto) {
        ContributorResponseDto updatedContributor = contributorService.updateContributor(contributorId, dto);
        return ResponseEntity.ok(updatedContributor);
    }

    @Operation(summary = "Delete a contributor", description = "특정 기여자를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "기여자 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "기여자를 찾을 수 없음"),
    })
    @DeleteMapping("/{contributorId}")
    public ResponseEntity<Void> deleteContributor(@PathVariable Long contributorId) {
        contributorService.deleteContributorById(contributorId);
        return ResponseEntity.noContent().build();
    }
}
