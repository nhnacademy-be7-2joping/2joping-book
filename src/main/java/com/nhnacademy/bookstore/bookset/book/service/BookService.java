package com.nhnacademy.bookstore.bookset.book.service;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookCreateResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.request.BookUpdateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookUpdateResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookUpdateResultResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BookService {
    BookCreateResponseDto createBook(BookCreateRequestDto bookCreateRequestDto);
    Page<BookSimpleResponseDto> getAllBooks(Pageable pageable);
    BookResponseDto getBookById(Long bookId);
    Page<BookSimpleResponseDto> getBooksByCategoryId(Pageable pageable, Long categoryId);
    Page<BookSimpleResponseDto> getBooksByContributorId(Pageable pageable, Long contributorId);
    List<ContributorResponseDto> getContributorList(String text);
    Category getLowestLevelCategory(String categoryText);
    List<TagResponseDto> associateBookWithTag(Book book, String text);
    BookUpdateResponseDto getUpdateBookByBookId(Long bookId);
    BookUpdateResultResponseDto updateBook(Long bookId, BookUpdateRequestDto bookUpdateRequestDto);
}
