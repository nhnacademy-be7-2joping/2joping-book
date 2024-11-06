package com.nhnacademy.bookstore.bookset.tag.service;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.tag.dto.TagRequestDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.entity.BookTag;
import com.nhnacademy.bookstore.bookset.tag.entity.Tag;
import com.nhnacademy.bookstore.bookset.tag.repository.TagRepository;
import com.nhnacademy.bookstore.bookset.tag.repository.BookTagRepository;

import com.nhnacademy.bookstore.common.error.exception.base.ConflictException;
import com.nhnacademy.bookstore.common.error.exception.base.NotFoundException;
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
    private final BookRepository bookRepository;
    private final BookTagRepository bookTagRepository;

    /**
     * 새로운 태그를 생성합니다.
     *
     * @param dto 태그 요청 데이터
     * @return 생성된 태그 정보
     */
    public TagResponseDto createTag(TagRequestDto dto) {
        if (tagRepository.findByName(dto.name()).isPresent()) {
            throw new ConflictException("이미 존재하는 태그입니다.");
        }
        Tag tag = new Tag(dto.name());
        Tag savedTag = tagRepository.save(tag);

        return new TagResponseDto(savedTag.getTagId(), savedTag.getName());
    }

    /**
     * 특정 책에 태그를 할당합니다.
     *
     * @param tagId 태그 ID
     * @param bookId 책 ID
     * @return 할당된 태그 정보

     */
    @Override
    public TagResponseDto assignedTagToBook(Long tagId, Long bookId) {
        if (bookTagRepository.existsByBook_BookIdAndTag_TagId(bookId, tagId)) {
            throw new ConflictException("책에 이미 태그가 할당되어 있습니다.");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 책입니다."));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 태그입니다."));

        BookTag bookTag = new BookTag(new BookTag.BookTagId(bookId, tagId), book, tag);
        bookTagRepository.save(bookTag);

        return new TagResponseDto(tag.getTagId(), tag.getName());
    }

    /**
     * 특정 태그를 조회합니다.
     *
     * @param tagId 태그 ID
     * @return 조회된 태그 정보
     */
    @Override
    public TagResponseDto getTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 태그입니다."));

        return new TagResponseDto(tag.getTagId(), tag.getName());
    }



    /**
     * 모든 태그를 조회합니다.
     *
     * @return 모든 태그 정보 리스트
     */
    @Override
    public List<TagResponseDto> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> new TagResponseDto(tag.getTagId(), tag.getName()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 태그를 업데이트합니다.
     *
     * @param tagId 태그 ID
     * @param dto 업데이트할 태그 데이터
     * @return 업데이트된 태그 정보
     */
    @Override
    public TagResponseDto updateTag(Long tagId, TagRequestDto dto) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 태그입니다."));

        tag.updateTag(dto.name());
        Tag updatedTag = tagRepository.save(tag);
        return new TagResponseDto(updatedTag.getTagId(), updatedTag.getName());
    }

    /**
     * 특정 태그를 삭제합니다.
     *
     * @param tagId 태그 ID
     */
    @Override
    public void deleteById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 태그입니다."));
        tagRepository.delete(tag);
    }
}
