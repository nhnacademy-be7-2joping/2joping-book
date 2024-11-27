package com.nhnacademy.bookstore.bookset.category.service.impl;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryRequestDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.mapper.CategoryMapper;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.category.service.CategoryService;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CannotDeactivateCategoryException;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CategoryNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.DuplicateCategoryNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 카테고리 서비스 구현 클래스
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto request) {
        // 중복 이름 확인
        categoryRepository.findByName(request.categoryName()).ifPresent(existing -> {
            throw new DuplicateCategoryNameException();
        });

        Category parentCategory = null;
        if (request.parentCategoryId() != null) {
            parentCategory = categoryRepository.findById(request.parentCategoryId())
                    .orElseThrow(CategoryNotFoundException::new);
        }
        Category category = new Category(null, parentCategory, request.categoryName(), true);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponseDto(savedCategory);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        return  categoryMapper.toCategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto getParentCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        if (category.getParentCategory() == null) {
            throw new CategoryNotFoundException();
        }
        return categoryMapper.toCategoryResponseDto(category.getParentCategory());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto getGrandparentCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        Category parentCategory = category.getParentCategory();
        if (parentCategory == null || parentCategory.getParentCategory() == null) {
            throw new CategoryNotFoundException();
        }
        return categoryMapper.toCategoryResponseDto(parentCategory.getParentCategory());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> getChildCategories(Long categoryId) {
        List<Category> childCategories = categoryRepository.findAllByParentCategory_CategoryId(categoryId);
        return childCategories.stream()
                .map(categoryMapper::toCategoryResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> getAllActiveCategories() {
        List<Category> categoryList = categoryRepository.findAllByIsActiveTrue();
        return categoryList.stream()
                .map(categoryMapper::toCategoryResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CategoryIsActiveResponseDto> getAllCategoriesPage(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toCategoryIsActiveResponseDto);
    }

    @Override
    public List<CategoryResponseDto> getTopCategories() {
        List<Category> topCategoryList = categoryRepository.findTopCategories();
        return topCategoryList.stream()
                .map(categoryMapper::toCategoryResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        // 이름 중복 확인 (현재 카테고리를 제외하고)
        categoryRepository.findByName(request.categoryName()).ifPresent(existing -> {
            if (!existing.getCategoryId().equals(categoryId)) {
                throw new DuplicateCategoryNameException();
            }
        });

        Category parentCategory = null;
        if (request.parentCategoryId() != null) {
            parentCategory = categoryRepository.findById(request.parentCategoryId())
                    .orElseThrow(CategoryNotFoundException::new);
        }
        category.updateName(request.categoryName());
        category.updateParentCategory(parentCategory);
        return categoryMapper.toCategoryResponseDto(category);
    }

    @Transactional
    @Override
    public Long deactivateCategory(Long categoryId) {

        // 하위 카테고리가 있는지 확인
        boolean hasChildCategories = categoryRepository.existsByParentCategory_CategoryId(categoryId);
        if (hasChildCategories) {
            throw new CannotDeactivateCategoryException();
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        category.deactivate();
        return category.getCategoryId();
    }

    @Transactional
    @Override
    public Long activateCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        category.activate();
        return category.getCategoryId();
    }
}
