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

public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;


    @Override
    @Transactional
    public TagResponseDto createTag(TagRequestDto dto) {
        // tagname으로 tag가 이미 있는지 확인
        tagRepository.findByName(dto.getName())
                .ifPresent(existingTag -> {
                    throw new TagAlreadyExistException();
                });
        Tag tag = new Tag(dto.getName()); //새로 만들어서
        Tag savedTag = tagRepository.save(tag); //저장
        return new TagResponseDto(savedTag.getTagId(), savedTag.getName());
    }

    //조회
    @Override
    @Transactional
    public TagResponseDto getTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException());

        return TagResponseDto.builder() //responsedto로 tag id와 tag name전달
                .tagId(tag.getTagId())
                .name(tag.getName())
                .build();
    }

    @Override
    @Transactional
    public List<TagResponseDto> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream() //List 반환시 stream사용
                .map(tag -> TagResponseDto.builder()
                        .tagId(tag.getTagId())
                        .name(tag.getName())
                        .build())
                .collect(Collectors.toList());
    }

    //수정
    @Override
    @Transactional
    public TagResponseDto updateTag(Long tagId, TagRequestDto dto) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException());

        tag.updateTag(dto.getName()); //수정
        Tag updatedTag = tagRepository.save(tag); //저장
       // return new TagResponseDto(updatedTag.getTagId(), updatedTag.getName());
        return TagResponseDto.builder()
                .tagId(tag.getTagId())
                .name(tag.getName())
                .build();
    }

    @Override
    @Transactional
    public void deleteById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException());
        tagRepository.delete(tag);

    }
}

