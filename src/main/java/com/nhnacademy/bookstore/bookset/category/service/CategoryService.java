package com.nhnacademy.bookstore.bookset.category.service;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryRequestDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    CategoryResponseDto createCategory(CategoryRequestDto request);

    CategoryResponseDto getCategory(Long categoryId);

    CategoryResponseDto getParentCategory(Long categoryId);

    CategoryResponseDto getGrandparentCategory(Long categoryId);

    List<CategoryResponseDto> getChildCategories(Long categoryId);

    List<CategoryResponseDto> getAllActiveCategories();

    Page<CategoryIsActiveResponseDto> getAllCategoriesPage(Pageable pageable);

    List<CategoryResponseDto> getTopCategories();

    CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto request);

    Long deactivateCategory(Long categoryId);

    Long activateCategory(Long categoryId);
}
