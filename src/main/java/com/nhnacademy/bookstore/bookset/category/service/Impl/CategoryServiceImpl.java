package com.nhnacademy.bookstore.bookset.category.service.Impl;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.bookstore.bookset.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetAllCategoriesResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetParentCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.dto.response.UpdateCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.category.service.CategoryService;
import com.nhnacademy.bookstore.common.error.exception.category.CategoryNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 카테고리 서비스 구현 클래스
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final int MAX_DEPTH = 3;

    /**
     * 카테고리 생성 메서드
     * @param request
     * @return 생성한 카테고리의 ID
     */
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

    /**
     * 카테고리 조회 메서드
     * @param categoryId
     * @return 조회한 단일 카테고리 DTO
     */
    @Override
    public GetCategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        return GetCategoryResponse.from(category);
    }

    @Override
    public GetParentCategoryResponse getParentCategory(Long categoryId) {
        Category childCategory = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        return GetParentCategoryResponse.from(childCategory);
    }

    // TODO: 모든 부모 카테고리 반환할 수 있도록 구현
    public List<Category> getAllParentCategories() {
        return null;
    }

    @Override
    public List<GetAllCategoriesResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByCategoryId().stream()
                .map(GetAllCategoriesResponse::from)
                .toList();
    }

    /**
     * 카테고리 수정 메서드
     * @param categoryId, request
     * @return 수정한 카테고리 DTO 객체
     */
    @Override
    public UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        if (request.parentCategory() != null) {
            validateCategoryDepth(request.parentCategory());
            Category newParent = categoryRepository.findByCategoryId(request.parentCategory().getCategoryId())
                    .orElseThrow(CategoryNotFoundException::new);
            category.updateParentCategory(newParent);
        }

        if (request.categoryName() != null) {
            category.updateName(request.categoryName());
        }

        return UpdateCategoryResponse.from(category);
    }

    /**
     * 카테고리 삭제 메서드
     * @param categoryId
     * @return 삭제한 카테고리의 ID
     */
    @Override
    public Long deleteCategory(Long categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

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
