package com.nhnacademy.bookstore.bookset.category.service;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryCreateRequest;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.category.service.Impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category parentCategory;
    private Category savedCategory;
    private CategoryCreateRequest request;
    private CategoryCreateRequest requestWithParent;

    @BeforeEach
    public void setUp() {
        parentCategory = Category.builder()
                .name("테스트 부모 카테고리")
                .parentCategory(null)
                .build();

        savedCategory = Category.builder()
                .name("테스트 카테고리")
                .parentCategory(null)
                .build();

        try {
            var idField = Category.class.getDeclaredField("categoryId");
            idField.setAccessible(true);
            idField.set(parentCategory, 1L);
            idField.set(savedCategory, 2L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        request = new CategoryCreateRequest(null, "테스트 카테고리");
        requestWithParent = new CategoryCreateRequest(parentCategory, "테스트 자식 카테고리");
    }

    @Test
    @DisplayName("새로운 카테고리 성공 테스트 - 상위 카테고리 없음")
    void createCategory_WithoutParentCategory_Success() {
        // given
        given(categoryRepository.save(any(Category.class))).willReturn(savedCategory);

        //when
        Long resultId = categoryService.createCategory(request);

        // then
        assertThat(resultId).isEqualTo(2L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("새로운 카테고리 성공 테스트 - 상위 카테고리 있음")
    void createCategory_WithParentCategory_Success() {
        // given
        Category childCategory = Category.builder()
                .name("자식 카테고리")
                .parentCategory(parentCategory)
                .build();

        try {
            var idField = Category.class.getDeclaredField("categoryId");
            idField.setAccessible(true);
            idField.set(childCategory, 3L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        given(categoryRepository.findByCategoryId(1L)).willReturn(Optional.ofNullable(parentCategory));
        given(categoryRepository.save(any(Category.class))).willReturn(childCategory);

        // when
        Long resultId = categoryService.createCategory(requestWithParent);

        // then
        assertThat(resultId).isEqualTo(3L);
        verify(categoryRepository).findByCategoryId(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("새로운 카테고리 생성 테스트 - 상위 카테고리가 존재하지 않을 경우")
    void createCategory_WithNonExistentParentCategory_Success() {
        // given
        given(categoryRepository.findByCategoryId(1L)).willReturn(Optional.empty());
        given(categoryRepository.save(any(Category.class))).willReturn(savedCategory);

        CategoryCreateRequest requestWithNonExistentParent =
                new CategoryCreateRequest(parentCategory, "테스트 카테고리");

        // when
        Long resultId = categoryService.createCategory(requestWithNonExistentParent);

        // then
        assertThat(resultId).isEqualTo(2L);
        verify(categoryRepository).findByCategoryId(1L);
        verify(categoryRepository).save(any(Category.class));
    }
}
