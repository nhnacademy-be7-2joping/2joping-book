package com.nhnacademy.bookstore.bookset.category.controller;

import com.nhnacademy.bookstore.bookset.category.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    public ResponseEntity<GetCategoryResponse> getCategory(@Valid @PathVariable Long categoryId) {
        GetCategoryResponse response =categoryService.getCategory(categoryId);
        return ResponseEntity.ok(response);
    }


}
