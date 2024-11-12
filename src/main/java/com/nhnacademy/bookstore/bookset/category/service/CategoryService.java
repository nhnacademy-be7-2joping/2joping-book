package com.nhnacademy.bookstore.bookset.category.service;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.bookstore.bookset.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetAllCategoriesResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.UpdateCategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 카테고리 서비스 인터페이스
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
@Service
public interface CategoryService {

    Long createCategory(CategoryCreateRequest request);
    GetCategoryResponse getCategory(Long categoryId);
    List<GetAllCategoriesResponse> getAllCategories();
    UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request);
    Long deleteCategory(Long categoryId);
}
