package com.nhnacademy.bookstore.bookset.category.controller;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.bookstore.bookset.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetAllCategoriesResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetParentCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.UpdateCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.service.CategoryService;
import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * 카테고리 컨트롤러
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
@RestController
@RequestMapping("/api/v1/bookstore")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 생성 메서드
     * @return 카테고리 생성 완료 상태 코드
     */
    @PostMapping("/categories")
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        Long categoryId = categoryService.createCategory(request);
        return ResponseEntity.created(URI.create("/" + categoryId)).build();
    }

    /**
     * 카테고리 조회 메서드
     * @param categoryId
     * @return 조회한 카테고리 DTO 객체
     */
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<GetCategoryResponse> getCategory(@ValidPathVariable @PathVariable Long categoryId) {
        GetCategoryResponse response = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    /**
     * 부모 카테고리 조회
     * @return 조회한 부모 카테고리 리스트 DTO
     */
    @GetMapping("/categories/{categoryId}/parents/")
    public ResponseEntity<GetParentCategoryResponse> getParentCategory(
            @ValidPathVariable @PathVariable Long categoryId
    ) {
        GetParentCategoryResponse response = categoryService.getParentCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    /**
     * 카테고리 전체 조회 메서드
     * @return 조회한 전체 카테고리 리스트 DTO
     */
    @GetMapping("/categories")
    public ResponseEntity<List<GetAllCategoriesResponse>> getAllCategories() {
        List<GetAllCategoriesResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    /**
     * 카테고리 업데이트 메서드
     * @param categoryId
     * @return 수정된 카테고리 DTO 객체
     */
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
        @ValidPathVariable @PathVariable Long categoryId,
        @RequestBody UpdateCategoryRequest request
    ) {
        UpdateCategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 카테고리 삭제 메서드
     * @param categoryId
     * @return 삭제한 카테고리의 ID
     */
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Long> deleteCategory(@ValidPathVariable @PathVariable Long categoryId) {
        Long deletedCategoryId = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(deletedCategoryId);
    }
}
