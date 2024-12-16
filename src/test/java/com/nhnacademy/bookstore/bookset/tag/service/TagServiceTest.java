package com.nhnacademy.bookstore.bookset.tag.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.tag.dto.TagRequestDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.entity.Tag;
import com.nhnacademy.bookstore.bookset.tag.repository.BookTagRepository;
import com.nhnacademy.bookstore.bookset.tag.repository.TagRepository;
import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.tag.TagAlreadyAssignedBookException;
import com.nhnacademy.bookstore.common.error.exception.bookset.tag.TagNotFoundException;
import com.nhnacademy.bookstore.like.entity.Like;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private BookTagRepository bookTagRepository;

    @Mock
    private BookRepository bookRepository;


    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTag_Success() {
        TagRequestDto requestDto = new TagRequestDto("New Tag");
        Tag savedTag = new Tag(1L, "New Tag");

        when(tagRepository.save(any(Tag.class))).thenReturn(savedTag);
        TagResponseDto responseDto = tagService.createTag(requestDto);

        assertThat(responseDto.tagId()).isEqualTo(savedTag.getTagId());
        assertThat(responseDto.name()).isEqualTo(savedTag.getName());
    }

    @Test
    void testAssignedTagToBook_Success() {
        Long tagId = 1L;
        Long bookId = 2L;
        Tag tag = new Tag(tagId, "Sample Tag");

        // likedBy를 빈 리스트로 초기화
        List<Like> likedBy = new ArrayList<>();

        Book book = new Book(
                bookId, null, "Sample Book", "A description", LocalDate.of(2020, 1, 1),
                "1234567890123", 1000, 900, true, true, 10, 100, 50, likedBy
        );

        when(bookTagRepository.existsByBook_BookIdAndTag_TagId(bookId, tagId)).thenReturn(false);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        TagResponseDto responseDto = tagService.assignedTagToBook(tagId, bookId);

        assertThat(responseDto.tagId()).isEqualTo(tagId);
        assertThat(responseDto.name()).isEqualTo(tag.getName());
    }

    @Test
    void testAssignedTagToBook_TagAlreadyAssigned() {
        Long tagId = 1L;
        Long bookId = 2L;

        when(bookTagRepository.existsByBook_BookIdAndTag_TagId(bookId, tagId)).thenReturn(true);

        assertThatThrownBy(() -> tagService.assignedTagToBook(tagId, bookId))
                .isInstanceOf(TagAlreadyAssignedBookException.class); // 수정된 예외 타입 반영
    }

    @Test
    void testCreateTag_TagAlreadyExists() {
        TagRequestDto requestDto = new TagRequestDto("Existing Tag");
        Tag existingTag = new Tag(1L, "Existing Tag");

        when(tagRepository.findByName(requestDto.name())).thenReturn(Optional.of(existingTag));
        assertThatThrownBy(() -> tagService.createTag(requestDto))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void testGetTag_TagExists() {
        Tag tag = new Tag(2L, "Test Tag");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));

        TagResponseDto responseDto = tagService.getTag(2L);

        assertThat(responseDto.tagId()).isEqualTo(tag.getTagId());
        assertThat(responseDto.name()).isEqualTo(tag.getName());
    }

    @Test
    void testGetTag_TagNotFound() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.getTag(1L))
                .isInstanceOf(TagNotFoundException.class);
    }

    @Test
    void testGetAllTags_ReturnTagList() {
        List<Tag> tags = List.of(new Tag(1L, "Tag1"), new Tag(2L, "Tag2"));
        when(tagRepository.findAll()).thenReturn(tags);

        List<TagResponseDto> responseDtos = tagService.getAllTags();

        assertThat(responseDtos).hasSize(2);
        assertThat(responseDtos.get(0).name()).isEqualTo("Tag1");
        assertThat(responseDtos.get(1).name()).isEqualTo("Tag2");
    }

    @Test
    void testUpdateTag_Success() {
        Tag tag = new Tag(3L, "Tag");
        TagRequestDto requestDto = new TagRequestDto("Updated Tag");

        when(tagRepository.findById(3L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag(3L, "Updated Tag"));

        TagResponseDto responseDto = tagService.updateTag(3L, requestDto);

        assertThat(responseDto.tagId()).isEqualTo(3L);
        assertThat(responseDto.name()).isEqualTo("Updated Tag");
    }

    @Test
    void testUpdateTag_TagNotFound() {
        TagRequestDto requestDto = new TagRequestDto("Updated Tag");
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty()); // 이것만 필요

        assertThatThrownBy(() -> tagService.updateTag(1L, requestDto))
                .isInstanceOf(NotFoundException.class);

        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void TestDeleteTag_Success() {
        Tag tag = new Tag(4L, "Delete Tag");
        when(tagRepository.findById(4L)).thenReturn(Optional.of(tag));
        doNothing().when(tagRepository).delete(tag);

        tagService.deleteById(4L);

        verify(tagRepository, times(1)).delete(tag);
    }

    @Test
    void testDeleteTag_TagNotFound() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.deleteById(1L))
                .isInstanceOf(NotFoundException.class);
    }
}
