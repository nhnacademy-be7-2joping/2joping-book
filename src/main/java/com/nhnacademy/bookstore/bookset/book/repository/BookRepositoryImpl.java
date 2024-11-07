package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.bookset.book.entity.QBookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.QBookContributor;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class BookRepositoryImpl extends QuerydslRepositorySupport implements BookRepositoryCustom {

    public BookRepositoryImpl() {
        super(Book.class);
    }


    /**
     * 전체 도서를 페이지 단위로 조회
     *
     * @param pageable 페이징 정보를 담고 있는 객체
     * @return 전체 도서 목록과 페이징 정보를 담은 Page 객체
     */

    @Override
    public Page<BookSimpleResponseDto> findAllBooks(Pageable pageable) {
        QBook qBook = QBook.book;

        List<BookSimpleResponseDto> booksDto = from(qBook)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(Projections.constructor(
                        BookSimpleResponseDto.class,
                        qBook.bookId,
                        Expressions.constant("temp.jpg"),
                        qBook.title,
                        qBook.sellingPrice,
                        qBook.publisher.name,
                        qBook.retailPrice,
                        qBook.isActive
                ))
                .fetch();

        long total = from(qBook)
                .fetchCount();

        return new PageImpl<>(booksDto, pageable, total);
    }


    /**
     * 특정 기여자가 참여한 도서를 페이지 단위로 조회
     *
     * @param pageable 페이징 정보를 담고 있는 객체
     * @param contributorId 조회할 기여자의 ID
     * @return 기여자가 참여한 도서 목록과 페이징 정보를 담은 Page 객체
     */
    @Override
    public Page<BookSimpleResponseDto> findBooksByContributorId(Pageable pageable,Long contributorId) {
        QBook qBook = QBook.book;
        QBookContributor qBookContributor = QBookContributor.bookContributor;

        List<BookSimpleResponseDto> booksDto = from(qBookContributor)
                .join(qBookContributor.book, qBook)
                .where(qBookContributor.contributor.contributorId.eq(contributorId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(Projections.constructor(
                        BookSimpleResponseDto.class,
                        qBook.bookId,
                        Expressions.constant("temp.jpg"), // 썸네일 경로가 임시일 경우
                        qBook.title,
                        qBook.sellingPrice,
                        qBook.publisher,
                        qBook.retailPrice,
                        qBook.isActive
                ))
                .fetch();

        long total = from(qBookContributor)
                .join(qBookContributor.book, qBook)
                .where(qBookContributor.contributor.contributorId.eq(contributorId))
                .distinct()
                .fetchCount();

        return new PageImpl<>(booksDto, pageable, total);
    }




    /**
     * 특정 카테고리에 속하는 도서를 페이지 단위로 조회
     *
     * @param pageable 페이징 정보를 담고 있는 객체
     * @param categoryId 조회할 카테고리의 ID
     * @return 카테고리별 도서 목록과 페이징 정보를 담은 Page 객체
     */
    @Override
    public Page<BookSimpleResponseDto> findBooksByCategoryId(Pageable pageable, Long categoryId) {
        QBook qBook = QBook.book;
        QBookCategory qBookCategory = QBookCategory.bookCategory;

        List<BookSimpleResponseDto> booksDto = from(qBookCategory)
                .join(qBookCategory.book, qBook)
                .where(qBookCategory.category.categoryId.eq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(Projections.constructor(
                        BookSimpleResponseDto.class,
                        qBook.bookId,
                        Expressions.constant("temp.jpg"), // 썸네일 경로가 임시일 경우
                        qBook.title,
                        qBook.sellingPrice,
                        qBook.publisher.name,
                        qBook.retailPrice,
                        qBook.isActive
                ))
                .fetch();


        long total = from(qBookCategory)
                .join(qBookCategory.book, qBook)
                .where(qBookCategory.category.categoryId.eq(categoryId))
                .distinct()
                .fetchCount();

        return new PageImpl<>(booksDto, pageable, total);
    }


    /**
     * 특정 도서의 상세 정보를 조회합니다.
     *
     * @param bookId 조회할 도서의 ID
     * @return 도서의 상세 정보를 담은 BookResponseDto 객체
     */
    @Override
    public BookResponseDto findBookByBookId(Long bookId) {
        QBook qBook = QBook.book;

        return from(qBook)
                .where(qBook.bookId.eq(bookId))
                .select(Projections.constructor(
                        BookResponseDto.class,
                        qBook.bookId,
                        qBook.publisher.name,
                        qBook.title,
                        qBook.description,
                        qBook.publishedDate,
                        qBook.isbn,
                        qBook.retailPrice,
                        qBook.sellingPrice,
                        qBook.giftWrappable,
                        qBook.isActive,
                        qBook.remainQuantity,
                        qBook.views,
                        qBook.likes,
                        Expressions.constant("temp.jpg") // 썸네일 경로가 임시일 경우
                ))
                .fetchOne();
    }
}


