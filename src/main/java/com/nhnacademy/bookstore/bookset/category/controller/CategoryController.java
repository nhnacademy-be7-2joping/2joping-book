package com.nhnacademy.bookstore.bookset.category.controller;

import com.nhnacademy.bookstore.bookset.category.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetAllCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.UpdateCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.service.CategoryService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<Void> createCategory(@RequestBody CreateCategoryRequest request) {
        Long categoryId = categoryService.createCategory(request);
        return ResponseEntity.created(URI.create("/" + categoryId)).build();
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<GetCategoryResponse> getCategory(@Positive @PathVariable Long categoryId) {
        GetCategoryResponse response = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<GetAllCategoryResponse>> getAllCategories() {
        List<GetAllCategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(@Positive @PathVariable Long categoryId,
                                           @RequestBody UpdateCategoryRequest request) {
        UpdateCategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Long> deleteCategory(@PathVariable Long categoryId) {
        Long deletedCategoryId = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(deletedCategoryId);
    }
}
