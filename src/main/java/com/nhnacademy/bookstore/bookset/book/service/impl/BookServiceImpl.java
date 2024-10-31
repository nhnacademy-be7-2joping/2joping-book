package com.nhnacademy.bookstore.bookset.book.service.impl;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    @Override
    public BookResponseDto createBook(BookCreateRequestDto request) {
        return new BookResponseDto();
    }


    /**
     * 전체 도서를 조회하는 메서드
     * @return 도서 객체
     */

    @Override
    public Page<BookSimpleResponseDto> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);

        return books.map(this::convertToBookSimpleResponseDto);
    }

    private BookSimpleResponseDto convertToBookSimpleResponseDto(Book book) {
        return new BookSimpleResponseDto(
                book.getBookId(),
                getBookThumbnail(book),
                book.getTitle(),
                book.getSellingPrice(),
                book.getPublisher(),
                book.getRetailPrice(),
                book.isActive()
        );
    }

    //TODO 썸네일 이미지파일 경로 수정해야함
    private String getBookThumbnail(Book book) {
        return "temp.jpg";
    }


    /**
     * 특정 도서를 조회하는 메서드
     * @param bookId
     * @return 도서 객체
     */

    @Override
    public Optional<BookResponseDto> getbookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));
        return Optional.ofNullable(convertToBookResponseDto(book));
    }


    /**
     * 카테고리로 도서를 조회하는 메서드
     * @param categoryId
     * @return 도서 객체
     */

    @Override
    public Page<BookSimpleResponseDto> getBooksByCategoryId(Pageable pageable, Long categoryId) {

        return bookRepository.findBooksByCategoryId(pageable, categoryId);
    }


//    @Override
//    public List<BookSimpleResponseDto> getBooksByCategoryId(Pageable pageable, Long categoryId) {
//        Page<Book> books = bookRepository.findBooksByCategoryId(pageable, categoryId);
//        List<BookSimpleResponseDto> responseDtoList = new ArrayList<>();
//
//        for (Book book : books) {
//            BookSimpleResponseDto dto = convertToBookSimpleResponseDto(book);
//            responseDtoList.add(dto);
//        }
//
//        return responseDtoList;
//    }

    private BookResponseDto convertToBookResponseDto(Book book) {
        return BookResponseDto.builder()
                .bookId(book.getBookId())
                .publisher(book.getPublisher())
                .title(book.getTitle())
                .description(book.getDescription())
                .publishedDate(book.getPublishedDate())
                .isbn(book.getIsbn())
                .retailPrice(book.getRetailPrice())
                .sellingPrice(book.getSellingPrice())
                .giftWrappable(book.isGiftWrappable())
                .isActive(book.isActive())
                .remainQuantity(book.getRemainQuantity())
                .views(book.getViews())
                .likes(book.getLikes())
                .thumbnail(getBookThumbnail(book)) // 임시로 단일 썸네일을 가져오는 메서드
                .build();
    }


}
