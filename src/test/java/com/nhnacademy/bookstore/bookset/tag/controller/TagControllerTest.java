package com.nhnacademy.bookstore.bookset.tag.controller;

import com.nhnacademy.bookstore.bookset.tag.dto.TagRequestDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    void createTag_SuccessResponse() throws Exception {

        TagRequestDto requestDto = new TagRequestDto("NewTag");
        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"NewTag\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void assignTagToBook_SuccessResponse() throws Exception {
        TagResponseDto responseDto = new TagResponseDto(1L, "Sample Tag");
        given(tagService.assignedTagToBook(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(1L)))
                .willReturn(responseDto);

        mockMvc.perform(post("/api/v1/book/{book-id}/tags", 1L)
                        .param("tagId", "1")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(1L))
                .andExpect(jsonPath("$.name").value("Sample Tag"));
    }


    @Test
    void getTag_SuccessResponse() throws Exception {

        TagResponseDto responseDto = new TagResponseDto(2L, "ReadTag");
        given(tagService.getTag(2L)).willReturn(responseDto);

        mockMvc.perform(get("/api/v1/tags/{tag-id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(2L))
                .andExpect(jsonPath("$.name").value("ReadTag"));

    }


    @Test
    void getAllTags_ShouldReturnTags() throws Exception {
        TagResponseDto tag1 = new TagResponseDto(1L, "Tag1");
        TagResponseDto tag2 = new TagResponseDto(2L, "Tag2");
        given(tagService.getAllTags()).willReturn(List.of(tag1, tag2));

        mockMvc.perform(get("/api/v1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Tag1"))
                .andExpect(jsonPath("$[1].tagId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Tag2"));
    }

    @Test
    void updateTag_SuccessResponse() throws Exception {

        TagRequestDto requestDto = new TagRequestDto("UpdatedTag");
        TagResponseDto responseDto = new TagResponseDto(3L, "UpdatedTag");

        given(tagService.updateTag(ArgumentMatchers.eq(3L), any(TagRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(put("/api/v1/tags/{tag-id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"UpdatedTag\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(3L))
                .andExpect(jsonPath("$.name").value("UpdatedTag"));
    }

    @Test
    void deleteTag_SuccessResponse() throws Exception {
        doNothing().when(tagService).deleteById(4L);

        mockMvc.perform(delete("/api/v1/tags/{tag-id}", 4L))
                .andExpect(status().isNoContent());
    }
}
