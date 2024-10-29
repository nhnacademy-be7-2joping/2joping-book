package com.nhnacademy.bookstore.bookset.tag.controller;

import com.nhnacademy.bookstore.bookset.tag.dto.TagRequestDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.service.TagService;
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
@RequestMapping("/bookstore/tag")
@RequiredArgsConstructor

public class TagController {
    private final TagService tagService;

    @Operation(summary = "create a new tag", description = "새로운 태그를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "태그 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
    })

    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(@RequestBody @Valid TagRequestDto dto) {
            TagResponseDto createdTag = tagService.createTag(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    @Operation(summary = "get a tag", description = "특정 태그를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "태그 조회 성공"),
            @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음"),
    })

    @GetMapping("/{tagId}")
    public ResponseEntity<TagResponseDto> getTag(@PathVariable @Valid TagRequestDto dto) {
        TagResponseDto createdTag = tagService.createTag(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    @Operation(summary = "get all tags", description = "모든 태그를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 태그 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/allTags")
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        List<TagResponseDto> allTags = tagService.getAllTags();

        return ResponseEntity.ok(allTags);
    }

    @Operation(summary = "Update a tag", description = "특정 태그의 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음"),
    })

    @PutMapping("/{tagId}")
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable Long tagId, @RequestBody @Valid TagRequestDto dto) {
        TagResponseDto updatedTag = tagService.updateTag(tagId, dto);
        return null;
    }



    @Operation(summary = "Delete a tag", description = "태그를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음"),
    })

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteById(tagId);
        return ResponseEntity.noContent().build();
    }
}
