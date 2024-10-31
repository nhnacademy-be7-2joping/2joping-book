package com.nhnacademy.bookstore.bookset.category.service;

import com.nhnacademy.bookstore.bookset.category.dto.request.CreateCategoryRequest;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    Long createCategory(CreateCategoryRequest request);
}
