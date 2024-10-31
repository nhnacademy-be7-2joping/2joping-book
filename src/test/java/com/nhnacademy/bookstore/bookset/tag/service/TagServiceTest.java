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

    @Test
    public void testCreateTag_Success() {
        TagRequestDto requestDto = new TagRequestDto("New Tag");
        Tag tag = new Tag(requestDto.name());
        Tag savedTag = new Tag(1L, "New Tag");

        when(tagRepository.save(any(Tag.class))).thenReturn(savedTag);
        TagResponseDto responseDto = tagService.createTag(requestDto);

        assertThat(responseDto.tagId()).isEqualTo(savedTag.getTagId());
        assertThat(responseDto.name()).isEqualTo(savedTag.getName());
    }

    @Test
    public void testCreateTag_TagAlreadyExists() {
        TagRequestDto requestDto = new TagRequestDto("Existing Tag");
        Tag existingTag = new Tag(1L, "Existing Tag");

        when(tagRepository.findByName(requestDto.name())).thenReturn(Optional.of(existingTag));
        assertThatThrownBy(() -> tagService.createTag(requestDto))
                .isInstanceOf(TagAlreadyExistException.class);
    }

    @Test
    public void testGetTag_TagExists() {
        Tag tag = new Tag(2L, "Test Tag");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));

        TagResponseDto responseDto = tagService.getTag(2L);

        assertThat(responseDto.tagId()).isEqualTo(tag.getTagId());
        assertThat(responseDto.name()).isEqualTo(tag.getName());
    }

    @Test
    public void testGetTag_TagNotFound() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.getTag(1L))
                .isInstanceOf(TagNotFoundException.class);
    }

    @Test
    public void testGetAllTags_ReturnTagList() {
        List<Tag> tags = List.of(new Tag(1L, "Tag1"), new Tag(2L, "Tag2"));
        when(tagRepository.findAll()).thenReturn(tags);

        List<TagResponseDto> responseDtos = tagService.getAllTags();

        assertThat(responseDtos).hasSize(2);
        assertThat(responseDtos.get(0).name()).isEqualTo("Tag1");
        assertThat(responseDtos.get(1).name()).isEqualTo("Tag2");
    }

    @Test
    public void testUpdateTag_Success() {
        Tag tag = new Tag(3L, "Tag");
        TagRequestDto requestDto = new TagRequestDto("Updated Tag");

        when(tagRepository.findById(3L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag(3L, "Updated Tag"));

        TagResponseDto responseDto = tagService.updateTag(3L, requestDto);

        assertThat(responseDto.tagId()).isEqualTo(3L);
        assertThat(responseDto.name()).isEqualTo("Updated Tag");
    }

    @Test
    public void testUpdateTag_TagNotFound() {
        TagRequestDto requestDto = new TagRequestDto("Updated Tag");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty()); // 이것만 필요

        assertThatThrownBy(() -> tagService.updateTag(1L, requestDto))
                .isInstanceOf(TagNotFoundException.class);

        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    public void TestDeleteTag_Success() {
        Tag tag = new Tag(4L, "Delete Tag");
        when(tagRepository.findById(4L)).thenReturn(Optional.of(tag));
        doNothing().when(tagRepository).delete(tag);

        tagService.deleteById(4L);

        verify(tagRepository, times(1)).delete(tag);
    }

    @Test
    public void testDeleteTag_TagNotFound() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.deleteById(1L))
                .isInstanceOf(TagNotFoundException.class);
    }
}
