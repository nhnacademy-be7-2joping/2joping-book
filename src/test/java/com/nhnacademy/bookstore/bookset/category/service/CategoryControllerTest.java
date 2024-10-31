package com.nhnacademy.bookstore.bookset.category.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.bookset.category.controller.CategoryController;
import com.nhnacademy.bookstore.bookset.category.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private Category parentCategory;
    private CreateCategoryRequest request;
    private CreateCategoryRequest requestWithParent;

    @BeforeEach
    void setUp() {
        parentCategory = Category.builder()
                .name("부모 카테고리")
                .subcategory(null)
                .build();

        try {
            var idField = Category.class.getDeclaredField("categoryId");
            idField.setAccessible(true);
            idField.set(parentCategory, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        request = new CreateCategoryRequest(null, "테스트 카테고리");
        requestWithParent = new CreateCategoryRequest(parentCategory, "자식 카테고리");
    }

    @Test
    @DisplayName("카테고리 생성 API 테스트 - 성공")
    void createCategory_Success() throws Exception {
        // given
        given(categoryService.createCategory(any(CreateCategoryRequest.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/1"));
    }

    @Test
    @DisplayName("카테고리 생성 API 테스트 - 상위 카테고리 포함")
    void createCategory_WithParentCategory_Success() throws Exception {
        // given
        given(categoryService.createCategory(any(CreateCategoryRequest.class))).willReturn(2L);

        // when & then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithParent)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/2"));
    }

    @Test
    @DisplayName("카테고리 생성 API 테스트 - 잘못된 요청 (카테고리 이름 없음)")
    void createCategory_BadRequest_NameIsEmpty() throws Exception {
        // given
        CreateCategoryRequest invalidRequest = new CreateCategoryRequest(null, "");

        // when & then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
