package com.nhnacademy.bookstore.bookset.category.service;

import com.nhnacademy.bookstore.bookset.category.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    Long createCategory(CreateCategoryRequest request);
    GetCategoryResponse getCategory(Long categoryId);
    List<GetCategoryResponse> getAllCategories();
    void updateCategory(Long categoryId, UpdateCategoryRequest request);
    void deleteCategory(Long categoryId);
}
