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
        // given
        TagResponseDto responseDto = new TagResponseDto(1L, "Tag1"); // 서버가 태그를 만들고 나서 응답으로 돌려줄 데이터
        //ID가 1이고 이름이 New Tag인 태그가 생성되었다고 알려주는 것
        given(tagService.createTag(ArgumentMatchers.any(TagRequestDto.class))).willReturn(responseDto); //서비스 계층의 동작을 가짜로 정의

        // when & then
        mockMvc.perform(post("/bookstore/tag") //post 동작
                        .contentType(MediaType.APPLICATION_JSON) //json형식으로/ 얘네는 서버로 전송할 요청 본문이 있을때만 필요함.
                        .content("{\"name\": \"Tag1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tagId").value(1L))
                .andExpect(jsonPath("$.name").value("Tag1"));
    }

    @Test
    public void getTag_SuccessResponse() throws Exception {

        TagResponseDto responseDto = new TagResponseDto(2L, "ReadTag");
        given(tagService.getTag(2L)).willReturn(responseDto); // given을 하면 will return을 해주겠따

        mockMvc.perform(get("/bookstore/tag/{tagId}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(2L))
                .andExpect(jsonPath("$.name").value("ReadTag"));

    }

    @Test
    public void getAllTags_ShouldReturnTags() throws Exception {
        // given
        TagResponseDto tag1 = new TagResponseDto(1L, "Tag1");
        TagResponseDto tag2 = new TagResponseDto(2L, "Tag2");
        given(tagService.getAllTags()).willReturn(List.of(tag1, tag2));

        // when & then
        mockMvc.perform(get("/bookstore/tag/allTags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Tag1"))
                .andExpect(jsonPath("$[1].tagId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Tag2"));
    }

    @Test
    public void updateTag_SuccessResponse() throws Exception {

        TagRequestDto requestDto = new TagRequestDto("UpdatedTag"); // 클라이언트가 서버로 보내는 수정 요청 데이터
        TagResponseDto responseDto = new TagResponseDto(3L, "UpdatedTag"); // update 된 이후의 결과

        given(tagService.updateTag(ArgumentMatchers.eq(3L), ArgumentMatchers.any(TagRequestDto.class))).willReturn(responseDto); // updatetag가 request dto를 받고 수정된 결과로 responsedto를 반환

        mockMvc.perform(put("/bookstore/tag/{tagId}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"UpdatedTag\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(3L))
                .andExpect(jsonPath("$.name").value("UpdatedTag"));
    }

    @Test
    public void deleteTag_SuccessResponse() throws Exception {
        //given
        doNothing().when(tagService).deleteById(4L);

        mockMvc.perform(delete("/bookstore/tag/{tagId}", 4L))
                .andExpect(status().isNoContent());
    }
}
