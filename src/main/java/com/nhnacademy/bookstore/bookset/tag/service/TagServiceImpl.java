package com.nhnacademy.bookstore.bookset.tag.service;


import com.nhnacademy.bookstore.bookset.tag.dto.TagRequestDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.entity.Tag;
import com.nhnacademy.bookstore.bookset.tag.exception.TagAlreadyExistException;
import com.nhnacademy.bookstore.bookset.tag.exception.TagNotFoundException;
import com.nhnacademy.bookstore.bookset.tag.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * tag service
 *
 * @author : 박채연
 * @date : 2024-10-27
 */

@Service
@RequiredArgsConstructor
@Transactional


public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;


    @Override
    public TagResponseDto createTag(TagRequestDto dto) {
        tagRepository.findByName(dto.name())
                .ifPresent(existingTag -> {
                    throw new TagAlreadyExistException();
                });
        Tag tag = new Tag(dto.name());
        Tag savedTag = tagRepository.save(tag);
        return new TagResponseDto(savedTag.getTagId(), savedTag.getName());
    }

    @Override
    public TagResponseDto getTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException());

        return new TagResponseDto(tag.getTagId(), tag.getName());
    }

    @Override
    public List<TagResponseDto> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> new TagResponseDto(tag.getTagId(), tag.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public TagResponseDto updateTag(Long tagId, TagRequestDto dto) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException());

        tag.updateTag(dto.name());
        Tag updatedTag = tagRepository.save(tag);
        return new TagResponseDto(updatedTag.getTagId(), updatedTag.getName());
    }

    @Override
    public void deleteById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException());
        tagRepository.delete(tag);

    }
}

