package com.nhnacademy.bookstore.bookset.category.mapper;

import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CategoryMapper {
    // Category Entity -> CategoryResponseDto 변환
    @Mapping(target = "parentCategoryId", source = "parentCategory.categoryId")
    CategoryResponseDto toCategoryResponseDto(Category category);

    // Category Entity -> CategoryIsActiveResponseDto 변환
    @Mapping(target = "parentCategoryId", source = "parentCategory.categoryId")
    CategoryIsActiveResponseDto toCategoryIsActiveResponseDto(Category category);
}
