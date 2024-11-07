package com.nhnacademy.bookstore.bookset.category.service.Impl;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.bookstore.bookset.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetAllCategoriesResponse;
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
    public Long createCategory(CategoryCreateRequest request) {
        Category parentCategory = null;
        if (request.parentCategory() != null) {
            parentCategory = categoryRepository.findByCategoryId(request.parentCategory().getCategoryId())
                    .orElse(null);
        }

        Category category = Category.builder()
                .parentCategory(parentCategory)
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
                category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null
        );
    }

    // TODO: GetAllCategoryResponse 구현
    @Override
    public List<GetAllCategoriesResponse> getAllCategories() {
        return null;
    }

    @Override
    public UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        if (request.parentCategory() != null) {
            validateCategoryDepth(request.parentCategory());
            Category newParent = categoryRepository.findByCategoryId(request.parentCategory().getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("상위 카테고리를 찾을 수 없습니다."));
            category.updateParentCategory(newParent);
        }

        if (request.categoryName() != null) {
            category.updateName(request.categoryName());
        }

        return UpdateCategoryResponse.from(category);
    }

    @Override
    public Long deleteCategory(Long categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        if (categoryRepository.findByParentCategory(category).isPresent()) {
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
        while (current.getParentCategory() != null) {
            depth++;
            if (depth >= MAX_DEPTH) {
                throw new IllegalStateException("카테고리 깊이는 " + MAX_DEPTH + "를 초과할 수 없습니다.");
            }
            current = current.getParentCategory();
        }
    }
}
