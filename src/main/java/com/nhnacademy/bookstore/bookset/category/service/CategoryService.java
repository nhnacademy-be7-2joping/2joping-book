package com.nhnacademy.bookstore.bookset.category.service;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.bookstore.bookset.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetAllCategoriesResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.UpdateCategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    Long createCategory(CategoryCreateRequest request);
    GetCategoryResponse getCategory(Long categoryId);
    List<GetAllCategoriesResponse> getAllCategories();
    UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request);
    Long deleteCategory(Long categoryId);
}
