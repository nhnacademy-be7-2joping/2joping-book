package com.nhnacademy.bookstore.bookset.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.bookset.category.dto.request.CategoryRequestDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryIsActiveResponseDto;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.category.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("카테고리 생성 테스트")
    void createCategory() throws Exception {
        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(null, "소설");
        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "소설", null);

        Mockito.when(categoryService.createCategory(any(CategoryRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookstore/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.name").value("소설"));
    }

    @Test
    @DisplayName("카테고리 조회 테스트")
    void getCategory() throws Exception {
        // given
        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "소설", null);

        Mockito.when(categoryService.getCategory(1L)).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.name").value("소설"));
    }

    @Test
    @DisplayName("최상위 카테고리 조회 테스트")
    void getTopCategories() throws Exception {
        // given
        List<CategoryResponseDto> responseDtos = List.of(
                new CategoryResponseDto(1L, "소설", null),
                new CategoryResponseDto(2L, "역사", null)
        );

        Mockito.when(categoryService.getTopCategories()).thenReturn(responseDtos);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/categories/top")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("소설"))
                .andExpect(jsonPath("$[1].name").value("역사"));
    }

    @Test
    @DisplayName("부모 카테고리 조회 테스트")
    void getParentCategory() throws Exception {
        // given
        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "문학", null);

        Mockito.when(categoryService.getParentCategory(2L)).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/categories/2/parent")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("문학"));
    }

    @Test
    @DisplayName("자식 카테고리 조회 테스트")
    void getChildCategories() throws Exception {
        // given
        List<CategoryResponseDto> responseDtos = List.of(
                new CategoryResponseDto(2L, "소설", 1L),
                new CategoryResponseDto(3L, "시", 1L)
        );

        Mockito.when(categoryService.getChildCategories(1L)).thenReturn(responseDtos);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/categories/1/children")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("소설"))
                .andExpect(jsonPath("$[1].name").value("시"));
    }

    @Test
    @DisplayName("카테고리 비활성화 테스트")
    void deactivateCategory() throws Exception {
        // given
        Long deactivatedCategoryId = 1L;

        Mockito.when(categoryService.deactivateCategory(1L)).thenReturn(deactivatedCategoryId);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/categories/1/deactivate")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("카테고리 활성화 테스트")
    void activateCategory() throws Exception {
        // given
        Long activatedCategoryId = 1L;

        Mockito.when(categoryService.activateCategory(1L)).thenReturn(activatedCategoryId);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/categories/1/activate")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("카테고리 조회 페이징 테스트")
    void getAllCategoriesPage() throws Exception {
        // given
        Page<CategoryIsActiveResponseDto> page = new PageImpl<>(
                List.of(new CategoryIsActiveResponseDto(1L, "소설", null, true)),
                PageRequest.of(0, 10),
                1
        );

        Mockito.when(categoryService.getAllCategoriesPage(any(PageRequest.class))).thenReturn(page);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/categories/pages")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("소설"));
    }

    @Test
    @DisplayName("할아버지 카테고리 조회 테스트")
    void getGrandparentCategory() throws Exception {
        // given
        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "문학", null);

        Mockito.when(categoryService.getGrandparentCategory(3L)).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/categories/3/grandparent")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.name").value("문학"));
    }

    @Test
    @DisplayName("활성화된 전체 카테고리 조회 테스트")
    void getAllActiveCategories() throws Exception {
        // given
        List<CategoryResponseDto> responseDtos = List.of(
                new CategoryResponseDto(1L, "문학", null),
                new CategoryResponseDto(2L, "역사", null)
        );

        Mockito.when(categoryService.getAllActiveCategories()).thenReturn(responseDtos);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookstore/categories")
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].categoryId").value(1L))
                .andExpect(jsonPath("$[0].name").value("문학"))
                .andExpect(jsonPath("$[1].categoryId").value(2L))
                .andExpect(jsonPath("$[1].name").value("역사"));
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    void updateCategory() throws Exception {
        // given
        CategoryRequestDto requestDto = new CategoryRequestDto(1L, "수정된 이름");
        CategoryResponseDto responseDto = new CategoryResponseDto(2L, "수정된 이름", 1L);

        Mockito.when(categoryService.updateCategory(eq(2L), any(CategoryRequestDto.class))).thenReturn(responseDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookstore/categories/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(2L))
                .andExpect(jsonPath("$.name").value("수정된 이름"))
                .andExpect(jsonPath("$.parentCategoryId").value(1L));
    }

}
