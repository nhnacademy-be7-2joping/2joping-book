package com.nhnacademy.bookstore.bookset.category.service.Impl;

import com.nhnacademy.bookstore.bookset.category.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.category.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Long createCategory(CreateCategoryRequest request) {
        Category subcategory = request.subcategoryId() != null ?
                categoryRepository.findByCategoryId(request.subcategoryId().getCategoryId()).orElse(null) : null;

        Category category = Category.builder()
                .subcategory(subcategory)
                .name(request.categoryName())
                .build();
        return categoryRepository.save(category).getCategoryId();
    }
}
