package com.nhnacademy.bookstore.bookset.tag.controller;

import com.nhnacademy.bookstore.bookset.tag.dto.TagRequestDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 태그 Controller
 *
 * @author : 박채연
 * @date : 2024-10-28
 */
@Tag(name = "Tag", description = "태그 API")
@Validated
@RestController
@RequiredArgsConstructor

public class TagController {
    private final TagService tagService;

    /**
     * 새로운 태그를 생성하는 컨트롤러입니다.
     *
     * @param dto 태그 요청 데이터
     * @return 생성 성공 상태
     */
    @Operation(summary = "create a new tag", description = "새로운 태그를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "태그 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping("api/v1/tags")
    public ResponseEntity<Void> createTag(@RequestBody @Valid TagRequestDto dto) {
        tagService.createTag(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 특정 책에 태그를 할당하는 컨트롤러입니다.
     *
     * @param tagId 태그 ID
     * @param bookId 도서 ID
     * @return 할당된 태그 정보
     */


    @Operation(summary = "Assign a tag to a book", description = "특정 책에 태그를 할당합니다.")
    @ApiResponse(responseCode = "200", description = "태그 할당 성공")
    @ApiResponse(responseCode = "404", description = "도서나 태그를 찾을 수 없음")
    @ApiResponse(responseCode = "409", description = "태그가 이미 도서에 할당됨")
    @PostMapping("api/v1/book/{book-id}/tags")
    public ResponseEntity<TagResponseDto> assignTagToBook(
            @PathVariable("book-id") @Positive Long bookId,
            @RequestParam @Positive Long tagId) {
        TagResponseDto assignedTag = tagService.assignedTagToBook(tagId, bookId);
        return ResponseEntity.ok(assignedTag);
    }

    /**
     * 특정 태그를 조회하는 컨트롤러입니다.
     *
     * @param tagId 태그 ID
     * @return 태그 정보
     */
    @Operation(summary = "get a tag", description = "특정 태그를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "태그 조회 성공")
    @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    @GetMapping("api/v1/tags/{tag-id}")
    public ResponseEntity<TagResponseDto> getTag(@PathVariable("tag-id") @Positive Long tagId) {
        TagResponseDto tag = tagService.getTag(tagId);
        return ResponseEntity.ok(tag);
    }

    /**
     * 모든 태그를 조회하는 컨트롤러입니다.
     *
     * @return 모든 태그 정보
     */
    @Operation(summary = "get all tags", description = "모든 태그를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "모든 태그 조회 성공")
    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    @GetMapping("api/v1/tags")
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        List<TagResponseDto> allTags = tagService.getAllTags();

        return ResponseEntity.ok(allTags);
    }

    /**
     * 특정 태그를 수정하는 컨트롤러입니다.
     *
     * @param tagId 태그 ID
     * @param dto 수정할 태그 데이터
     * @return 수정된 태그 정보
     */

    @Operation(summary = "Update a tag", description = "특정 태그의 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "태그 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    @PutMapping("api/v1/tags/{tag-id}")
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable ("tag-id") @Positive Long tagId, @RequestBody @Valid TagRequestDto dto) {
        TagResponseDto updatedTag = tagService.updateTag(tagId, dto);
        return ResponseEntity.ok(updatedTag);
    }

    /**
     * 특정 태그를 삭제하는 컨트롤러입니다.
     *
     * @param tagId 태그 ID
     * @return 삭제 성공 상태
     */
    @Operation(summary = "Delete a tag", description = "태그를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "태그 삭제 성공")
    @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    @DeleteMapping("api/v1/tags/{tag-id}")
    public ResponseEntity<Void> deleteTag(@PathVariable ("tag-id") @Positive Long tagId) {
        tagService.deleteById(tagId);
        return ResponseEntity.noContent().build();
    }
}
