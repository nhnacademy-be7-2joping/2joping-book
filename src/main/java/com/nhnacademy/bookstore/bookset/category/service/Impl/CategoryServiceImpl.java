package com.nhnacademy.bookstore.bookset.category.service.Impl;

import com.nhnacademy.bookstore.bookset.category.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetAllCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.UpdateCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.category.service.CategoryService;
import com.nhnacademy.bookstore.common.error.exception.category.CategoryNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final int MAX_DEPTH = 3;

    @Override
    public Long createCategory(CreateCategoryRequest request) {
        Category subcategory = null;
        if (request.subcategory() != null) {
            subcategory = categoryRepository.findByCategoryId(request.subcategory().getCategoryId())
                    .orElse(null);
        }

        Category category = Category.builder()
                .subcategory(subcategory)
                .name(request.categoryName())
                .build();

        return categoryRepository.save(category).getCategoryId();
    }

    @Override
    public GetCategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        return new GetCategoryResponse(
                category.getCategoryId(),
                category.getName(),
                category.getSubcategory() != null ? category.getSubcategory().getCategoryId() : null
        );
    }

    // TODO: GetAllCategoryResponse 구현
    @Override
    public List<GetAllCategoryResponse> getAllCategories() {
        return null;
    }

    @Override
    public UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        if (request.subcategory() != null) {
            validateCategoryDepth(request.subcategory());
            Category newParent = categoryRepository.findByCategoryId(request.subcategory().getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("상위 카테고리를 찾을 수 없습니다."));
            category.updateSubcategory(newParent);
        }

        if (request.categoryName() != null) {
            category.updateName(request.categoryName());
        }

        UpdateCategoryResponse response = UpdateCategoryResponse.from(category);
        return  response;
    }

    @Override
    public Long deleteCategory(Long categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        if (!categoryRepository.findBySubcategory(category).isEmpty()) {
            throw new IllegalStateException("하위 카테고리가 있는 카테고리는 삭제할 수 없습니다.");
        }

        categoryRepository.delete(category);
        return category.getCategoryId();
    }

    private void validateCategoryDepth(Category parent) {
        if (parent == null) {
            return;
        }

        int depth = 1;
        Category current = parent;
        while (current.getSubcategory() != null) {
            depth++;
            if (depth >= MAX_DEPTH) {
                throw new IllegalStateException("카테고리 깊이는 " + MAX_DEPTH + "를 초과할 수 없습니다.");
            }
            current = current.getSubcategory();
        }
    }
}
