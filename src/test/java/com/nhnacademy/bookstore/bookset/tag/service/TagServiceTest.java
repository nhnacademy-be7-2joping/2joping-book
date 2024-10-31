package com.nhnacademy.bookstore.bookset.tag.service;

import com.nhnacademy.bookstore.bookset.tag.dto.TagRequestDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.entity.Tag;
import com.nhnacademy.bookstore.bookset.tag.exception.TagAlreadyExistException;
import com.nhnacademy.bookstore.bookset.tag.exception.TagNotFoundException;
import com.nhnacademy.bookstore.bookset.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //태그 생성
    @Test
    public void testCreateTag_Success() {
        // given
        TagRequestDto requestDto = new TagRequestDto("New Tag");
        Tag tag = new Tag(requestDto.getName());
        Tag savedTag = new Tag(1L, "New Tag");

        when(tagRepository.save(any(Tag.class))).thenReturn(savedTag);
        // when
        TagResponseDto responseDto = tagService.createTag(requestDto);

        // then
        assertThat(responseDto.getTagId()).isEqualTo(savedTag.getTagId());
        assertThat(responseDto.getName()).isEqualTo(savedTag.getName());
    }

    //태그 생성 시 tag already exists
    @Test
    public void testCreateTag_TagAlreadyExists() {
        // given
        TagRequestDto requestDto = new TagRequestDto("Existing Tag");
        Tag existingTag = new Tag(1L, "Existing Tag");

        when(tagRepository.findByName(requestDto.getName())).thenReturn(Optional.of(existingTag));
        //tagrepo에 findbyname을 호출할 때, optional.of를 반환하도록 설정
        // when & then
        assertThatThrownBy(() -> tagService.createTag(requestDto))
                .isInstanceOf(TagAlreadyExistException.class);
    }

    // 태그 아이디 조회
    @Test
    public void testGetTag_TagExists() {
        // given
        Tag tag = new Tag(2L, "Test Tag");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));

        // when
        TagResponseDto responseDto = tagService.getTag(2L);

        // then
        assertThat(responseDto.getTagId()).isEqualTo(tag.getTagId());
        assertThat(responseDto.getName()).isEqualTo(tag.getName());
    }

    //조회 시 tag not found
    @Test
    public void testGetTag_TagNotFound() {
        // given
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tagService.getTag(1L))
                .isInstanceOf(TagNotFoundException.class);
    }

    // 모든 태그 조회
    @Test
    public void testGetAllTags_ReturnTagList() {
        // given
        List<Tag> tags = List.of(new Tag(1L, "Tag1"), new Tag(2L, "Tag2"));
        when(tagRepository.findAll()).thenReturn(tags);

        // when
        List<TagResponseDto> responseDtos = tagService.getAllTags();

        // then
        assertThat(responseDtos).hasSize(2);
        assertThat(responseDtos.get(0).getName()).isEqualTo("Tag1");
        assertThat(responseDtos.get(1).getName()).isEqualTo("Tag2");
    }

    // 태그 업데이트
    @Test
    public void testUpdateTag_Success() {
        // given
        Tag tag = new Tag(3L, "Tag");
        TagRequestDto requestDto = new TagRequestDto("Updated Tag");

        when(tagRepository.findById(3L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag(3L, "Updated Tag"));

        // when
        TagResponseDto responseDto = tagService.updateTag(3L, requestDto);

        // then
        assertThat(responseDto.getTagId()).isEqualTo(3L);
        assertThat(responseDto.getName()).isEqualTo("Updated Tag");
    }

    // update 시 tag not found
    @Test
    public void testUpdateTag_TagNotFound() {
        // given
        TagRequestDto requestDto = new TagRequestDto("Updated Tag");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty()); // 이것만 필요

        // when & then
        assertThatThrownBy(() -> tagService.updateTag(1L, requestDto))
                .isInstanceOf(TagNotFoundException.class);

        verify(tagRepository, never()).save(any(Tag.class));
    }

// 태그 삭제
    @Test
    public void TestDeleteTag_Success() {
        // given
        Tag tag = new Tag(4L, "Delete Tag");
        when(tagRepository.findById(4L)).thenReturn(Optional.of(tag));
        doNothing().when(tagRepository).delete(tag);

        // when
        tagService.deleteById(4L);

        // then
        verify(tagRepository, times(1)).delete(tag);
    }

    //삭제시 tag not found
    @Test
    public void testDeleteTag_TagNotFound() {
        // given
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tagService.deleteById(1L))
                .isInstanceOf(TagNotFoundException.class);
    }
}
