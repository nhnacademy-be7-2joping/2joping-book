package com.nhnacademy.bookstore.bookset.category.service.impl;

import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryRequestDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.mapper.CategoryMapper;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CannotDeactivateCategoryException;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CategoryNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.DuplicateCategoryNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("카테고리 생성 성공 테스트")
    void createCategory() {
        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(null, "소설");
        Category category = new Category(1L, null, "소설", true);

        when(categoryRepository.findByName("소설")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategoryResponseDto(any(Category.class)))
                .thenReturn(new CategoryResponseDto(1L, "소설", null));

        // when
        CategoryResponseDto responseDto = categoryService.createCategory(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.categoryId());
        assertEquals("소설", responseDto.name());
    }

    @Test
    @DisplayName("카테고리 생성 실패 - 이름 중복 테스트")
    void createCategoryDuplicateName() {
        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(null, "소설");
        when(categoryRepository.findByName("소설")).thenReturn(Optional.of(new Category()));

        // when & then
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.createCategory(requestDto));
    }

    @Test
    @DisplayName("카테고리 조회 성공 테스트")
    void getCategory() {
        // given
        Category category = new Category(1L, null, "소설", true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResponseDto(any(Category.class)))
                .thenReturn(new CategoryResponseDto(1L, "소설", null));

        // when
        CategoryResponseDto responseDto = categoryService.getCategory(1L);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.categoryId());
        assertEquals("소설", responseDto.name());
    }

    @Test
    @DisplayName("카테고리 조회 실패 테스트 - 카테고리 없음")
    void getCategoryNotFound() {
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory(1L));
    }

    @Test
    @DisplayName("카테고리 수정 성공 테스트")
    void updateCategory() {
        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(null, "역사");
        Category category = new Category(1L, null, "소설", true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByName("역사")).thenReturn(Optional.empty());
        when(categoryMapper.toCategoryResponseDto(any(Category.class)))
                .thenReturn(new CategoryResponseDto(1L, "역사", null));

        // when
        CategoryResponseDto responseDto = categoryService.updateCategory(1L, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.categoryId());
        assertEquals("역사", responseDto.name());
    }

    @Test
    @DisplayName("카테고리 수정 실패 - 이름 중복 테스트")
    void updateCategoryDuplicateName() {
        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(null, "역사");
        Category category = new Category(1L, null, "소설", true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByName("역사")).thenReturn(Optional.of(new Category(2L, null, "역사", true)));

        // when & then
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.updateCategory(1L, requestDto));
    }

    @Test
    @DisplayName("카테고리 비활성화 성공 테스트")
    void deactivateCategory() {
        // given
        Category category = new Category(1L, null, "소설", true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByParentCategory_CategoryId(1L)).thenReturn(false);

        // when
        Long deactivatedId = categoryService.deactivateCategory(1L);

        // then
        assertEquals(1L, deactivatedId);
        assertFalse(category.getIsActive());
    }

    @Test
    @DisplayName("카테고리 비활성화 실패 - 하위 카테고리 존재")
    void deactivateCategoryWithChildCategories() {
        // given
        when(categoryRepository.existsByParentCategory_CategoryId(1L)).thenReturn(true);

        // when & then
        assertThrows(CannotDeactivateCategoryException.class, () -> categoryService.deactivateCategory(1L));
    }

    @Test
    @DisplayName("활성화된 모든 카테고리 조회 테스트")
    void getAllActiveCategories() {
        // given
        List<Category> categories = List.of(new Category(1L, null, "소설", true));
        when(categoryRepository.findAllByIsActiveTrue()).thenReturn(categories);
        when(categoryMapper.toCategoryResponseDto(any(Category.class)))
                .thenReturn(new CategoryResponseDto(1L, "소설", null));

        // when
        List<CategoryResponseDto> responseDtos = categoryService.getAllActiveCategories();

        // then
        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.size());
        assertEquals("소설", responseDtos.getFirst().name());
    }

    @Test
    @DisplayName("모든 카테고리 페이징 조회 테스트")
    void getAllCategoriesPage() {
        // given
        Page<Category> page = new PageImpl<>(List.of(new Category(1L, null, "소설", true)));
        when(categoryRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(categoryMapper.toCategoryIsActiveResponseDto(any(Category.class)))
                .thenReturn(new CategoryIsActiveResponseDto(1L, "소설", null, true));

        // when
        Page<CategoryIsActiveResponseDto> responsePage = categoryService.getAllCategoriesPage(PageRequest.of(0, 10));

        // then
        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals("소설", responsePage.getContent().getFirst().name());
    }

    @Test
    @DisplayName("부모 카테고리 조회 성공 테스트")
    void getParentCategory() {
        // given
        Category parentCategory = new Category(1L, null, "문학", true);
        Category category = new Category(2L, parentCategory, "소설", true);

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResponseDto(parentCategory))
                .thenReturn(new CategoryResponseDto(1L, "문학", null));

        // when
        CategoryResponseDto responseDto = categoryService.getParentCategory(2L);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.categoryId());
        assertEquals("문학", responseDto.name());
    }

    @Test
    @DisplayName("부모 카테고리 조회 실패 테스트 - 부모 카테고리 없음")
    void getParentCategoryNotFound() {
        // given
        Category category = new Category(2L, null, "소설", true);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        // when & then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getParentCategory(2L));
    }

    @Test
    @DisplayName("할아버지 카테고리 조회 성공 테스트")
    void getGrandparentCategory() {
        // given
        Category grandparentCategory = new Category(1L, null, "문학", true);
        Category parentCategory = new Category(2L, grandparentCategory, "소설", true);
        Category category = new Category(3L, parentCategory, "한국 소설", true);

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResponseDto(grandparentCategory))
                .thenReturn(new CategoryResponseDto(1L, "문학", null));

        // when
        CategoryResponseDto responseDto = categoryService.getGrandparentCategory(3L);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.categoryId());
        assertEquals("문학", responseDto.name());
    }

    @Test
    @DisplayName("할아버지 카테고리 조회 실패 테스트 - 할아버지 카테고리 없음")
    void getGrandparentCategoryNotFound() {
        // given
        Category parentCategory = new Category(2L, null, "소설", true);
        Category category = new Category(3L, parentCategory, "한국 소설", true);

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));

        // when & then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getGrandparentCategory(3L));
    }

    @Test
    @DisplayName("자식 카테고리 조회 테스트")
    void getChildCategories() {
        // given
        List<Category> childCategories = List.of(
                new Category(2L, null, "소설", true),
                new Category(3L, null, "역사", true)
        );
        when(categoryRepository.findAllByParentCategory_CategoryId(1L)).thenReturn(childCategories);
        when(categoryMapper.toCategoryResponseDto(any(Category.class)))
                .thenReturn(new CategoryResponseDto(2L, "소설", 1L))
                .thenReturn(new CategoryResponseDto(3L, "역사", 1L));

        // when
        List<CategoryResponseDto> responseDtos = categoryService.getChildCategories(1L);

        // then
        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());
        assertEquals("소설", responseDtos.get(0).name());
        assertEquals("역사", responseDtos.get(1).name());
    }

    @Test
    @DisplayName("최상위 카테고리 조회 테스트")
    void getTopCategories() {
        // given
        List<Category> topCategories = List.of(
                new Category(1L, null, "문학", true),
                new Category(2L, null, "과학", true)
        );
        when(categoryRepository.findTopCategories()).thenReturn(topCategories);
        when(categoryMapper.toCategoryResponseDto(any(Category.class)))
                .thenReturn(new CategoryResponseDto(1L, "문학", null))
                .thenReturn(new CategoryResponseDto(2L, "과학", null));

        // when
        List<CategoryResponseDto> responseDtos = categoryService.getTopCategories();

        // then
        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());
        assertEquals("문학", responseDtos.get(0).name());
        assertEquals("과학", responseDtos.get(1).name());
    }

    @Test
    @DisplayName("카테고리 활성화 성공 테스트")
    void activateCategory() {
        // given
        Category category = new Category(1L, null, "문학", false);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // when
        Long activatedCategoryId = categoryService.activateCategory(1L);

        // then
        assertEquals(1L, activatedCategoryId);
        assertTrue(category.getIsActive());
    }

    @Test
    @DisplayName("카테고리 생성 실패 테스트 - 부모 카테고리 ID가 존재하지 않음")
    void createCategoryWithNonExistingParent() {
        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(999L, "소설");

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.createCategory(requestDto));
    }

    @Test
    @DisplayName("할아버지 카테고리 조회 실패 테스트 - 부모 카테고리 없음")
    void getGrandparentCategoryWithNoParent() {
        // given
        Category category = new Category(1L, null, "소설", true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // when & then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getGrandparentCategory(1L));
    }

    @Test
    @DisplayName("할아버지 카테고리 조회 실패 테스트 - 할아버지 카테고리 없음")
    void getGrandparentCategoryWithNoGrandparent() {
        // given
        Category parentCategory = new Category(2L, null, "문학", true);
        Category category = new Category(1L, parentCategory, "소설", true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // when & then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getGrandparentCategory(1L));
    }

    @Test
    @DisplayName("카테고리 수정 실패 테스트 - 부모 카테고리 ID가 존재하지 않음")
    void updateCategoryWithNonExistingParent() {
        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(999L, "역사");
        Category category = new Category(1L, null, "소설", true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(1L, requestDto));
    }

    @Test
    @DisplayName("카테고리 수정 성공 테스트 - 중복된 이름이 동일한 카테고리 ID일 때")
    void updateCategoryWithSameNameAndId() {
        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(null, "소설");
        Category category = new Category(1L, null, "소설", true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByName("소설")).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResponseDto(any(Category.class)))
                .thenReturn(new CategoryResponseDto(1L, "소설", null));

        // when
        CategoryResponseDto responseDto = categoryService.updateCategory(1L, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.categoryId());
        assertEquals("소설", responseDto.name());
    }
}

