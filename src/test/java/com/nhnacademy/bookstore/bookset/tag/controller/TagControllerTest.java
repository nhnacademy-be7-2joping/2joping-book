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
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void createTag_SuccessResponse() throws Exception {
        TagResponseDto responseDto = new TagResponseDto(1L, "Tag1");
        given(tagService.createTag(ArgumentMatchers.any(TagRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/bookstore/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Tag1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tagId").value(1L))
                .andExpect(jsonPath("$.name").value("Tag1"));
    }

    @Test
    public void getTag_SuccessResponse() throws Exception {

        TagResponseDto responseDto = new TagResponseDto(2L, "ReadTag");
        given(tagService.getTag(2L)).willReturn(responseDto);

        mockMvc.perform(get("/bookstore/tag/{tagId}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(2L))
                .andExpect(jsonPath("$.name").value("ReadTag"));

    }

    @Test
    public void getAllTags_ShouldReturnTags() throws Exception {
        TagResponseDto tag1 = new TagResponseDto(1L, "Tag1");
        TagResponseDto tag2 = new TagResponseDto(2L, "Tag2");
        given(tagService.getAllTags()).willReturn(List.of(tag1, tag2));

        mockMvc.perform(get("/bookstore/tag/allTags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Tag1"))
                .andExpect(jsonPath("$[1].tagId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Tag2"));
    }

    @Test
    public void updateTag_SuccessResponse() throws Exception {

        TagRequestDto requestDto = new TagRequestDto("UpdatedTag");
        TagResponseDto responseDto = new TagResponseDto(3L, "UpdatedTag");

        given(tagService.updateTag(ArgumentMatchers.eq(3L), ArgumentMatchers.any(TagRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(put("/bookstore/tag/{tagId}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"UpdatedTag\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(3L))
                .andExpect(jsonPath("$.name").value("UpdatedTag"));
    }

    @Test
    public void deleteTag_SuccessResponse() throws Exception {
        doNothing().when(tagService).deleteById(4L);

        mockMvc.perform(delete("/bookstore/tag/{tagId}", 4L))
                .andExpect(status().isNoContent());
    }
}
