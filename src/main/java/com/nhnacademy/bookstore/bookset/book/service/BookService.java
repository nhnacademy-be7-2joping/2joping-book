package com.nhnacademy.bookstore.bookset.book.service;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookCreateAPIResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookCreateResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface BookService {
    BookCreateResponseDto createBook(BookCreateRequestDto bookCreateRequestDto);
    List<BookCreateAPIResponseDto> createBooks();
    Page<BookSimpleResponseDto> getAllBooks(Pageable pageable);
    BookResponseDto getBookById(Long bookId);
    Page<BookSimpleResponseDto> getBooksByCategoryId(Pageable pageable, Long categoryId);
    Page<BookSimpleResponseDto> getBooksByContributorId(Pageable pageable, Long contributorId);
    List<ContributorResponseDto> getContributorListForAPI(String text);
    Category getLowestLevelCategory(String categoryText);
    List<ContributorResponseDto> getContributorList(String contributorListJson);
    // List<TagResponseDto> associateBookWithTag(Book book, String text);
}
