package com.nhnacademy.bookstore.bookset.category.controller;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryRequestDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.category.service.CategoryService;
import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 카테고리 컨트롤러
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
@Tag(name = "Category", description = "카테고리 관리 API")
@RestController
@RequestMapping("/api/v1/bookstore")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "카테고리 생성 성공")
    @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터")
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto request) {
        CategoryResponseDto response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "카테고리 조회", description = "카테고리 ID로 카테고리를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 조회 성공")
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategory(@ValidPathVariable @PathVariable Long categoryId) {
        CategoryResponseDto response = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "최상위 카테고리 조회", description = "최상위 카테고리를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 조회 성공")
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    @GetMapping("/categories/top")
    public ResponseEntity<List<CategoryResponseDto>> getTopCategories() {
        List<CategoryResponseDto> response = categoryService.getTopCategories();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "부모 카테고리 조회", description = "특정 카테고리의 부모 카테고리를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "부모 카테고리 조회 성공")
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    @GetMapping("/categories/{categoryId}/parent")
    public ResponseEntity<CategoryResponseDto> getParentCategory(@ValidPathVariable @PathVariable Long categoryId) {
        CategoryResponseDto response = categoryService.getParentCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "할아버지 카테고리 조회", description = "특정 카테고리의 할아버지 카테고리를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "할아버지 카테고리 조회 성공")
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    @GetMapping("/categories/{categoryId}/grandparent")
    public ResponseEntity<CategoryResponseDto> getGrandparentCategory(@ValidPathVariable @PathVariable Long categoryId) {
        CategoryResponseDto response = categoryService.getGrandparentCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "자식 카테고리 조회", description = "특정 카테고리의 자식 카테고리 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "자식 카테고리 조회 성공")
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    @GetMapping("/categories/{categoryId}/children")
    public ResponseEntity<List<CategoryResponseDto>> getChildCategories(@ValidPathVariable @PathVariable Long categoryId) {
        List<CategoryResponseDto> response = categoryService.getChildCategories(categoryId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 활성 카테고리 조회", description = "모든 활성화된 카테고리를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "전체 활성 카테고리 조회 성공")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getAllActiveCategories() {
        List<CategoryResponseDto> response = categoryService.getAllActiveCategories();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 카테고리 조회(페이징)", description = "모든 카테고리를 조회합니다.(페이징)")
    @ApiResponse(responseCode = "200", description = "전체 카테고리 조회 성공")
    @GetMapping("/categories/pages")
    public ResponseEntity<Page<CategoryIsActiveResponseDto>> getAllCategoriesPage(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CategoryIsActiveResponseDto> response = categoryService.getAllCategoriesPage(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 수정", description = "특정 카테고리를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공")
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @ValidPathVariable @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDto request
    ) {
        CategoryResponseDto response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 비활성화", description = "특정 카테고리를 비활성화합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 비활성화 성공")
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    @ApiResponse(responseCode = "400", description = "비활성화할 수 없는 카테고리")
    @PutMapping("/categories/{categoryId}/deactivate")
    public ResponseEntity<Long> deactivateCategory(@ValidPathVariable @PathVariable Long categoryId) {
        Long deactivatedCategoryId = categoryService.deactivateCategory(categoryId);
        return ResponseEntity.ok(deactivatedCategoryId);
    }

    @Operation(summary = "카테고리 활성화", description = "비활성화된 카테고리를 활성화합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 활성화 성공")
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    @PutMapping("/categories/{categoryId}/activate")
    public ResponseEntity<Long> activateCategory(@ValidPathVariable @PathVariable Long categoryId) {
        Long activatedCategoryId = categoryService.activateCategory(categoryId);
        return ResponseEntity.ok(activatedCategoryId);
    }
}



