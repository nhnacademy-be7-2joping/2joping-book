package com.nhnacademy.bookstore.bookset.tag.service;

import com.nhnacademy.bookstore.bookset.tag.dto.TagRequestDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    TagResponseDto createTag(TagRequestDto dto);

    TagResponseDto assignedTagToBook(Long tagId, Long bookId);

    TagResponseDto getTag(Long tagId);

    List<TagResponseDto> getAllTags();

    TagResponseDto updateTag(Long tagId, TagRequestDto dto);

    void deleteById(Long tagId);
}
