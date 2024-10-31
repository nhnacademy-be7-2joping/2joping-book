package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.bookset.book.entity.QBookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.QBookContributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.QContributor;
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
        super(Book.class); // Book 엔티티 클래스 설정
    }

//    @Override
//    public Page<Contributor> findContributorsByBookId(Long bookId, Pageable pageable) {
//        // Q 클래스 import
//        QBook qBook = QBook.book;
//        QBookContributor qBookContributor = QBookContributor.bookContributor;
//        QContributor qContributor = QContributor.contributor;
//
//        // QueryDSL로 쿼리 작성
//        List<Contributor> contributors = from(qBookContributor)
//                .join(qBookContributor.contributor, qContributor)
//                .join(qBookContributor.book, qBook)
//                .where(qBook.bookId.eq(bookId))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .select(qContributor)
//                .fetch();
//
//        // 총 데이터 수 계산
//        long total = from(qBookContributor)
//                .join(qBookContributor.contributor, qContributor)
//                .join(qBookContributor.book, qBook)
//                .where(qBook.bookId.eq(bookId))
//                .select(qContributor.count())
//                .fetchOne();
//
//        // Page 객체로 변환하여 반환
//        return new PageImpl<>(contributors, pageable, total);
//    }

    @Override
    public Page<BookSimpleResponseDto> findBooksByCategoryId(Pageable pageable, Long categoryId) {
        QBook qBook = QBook.book;
        QBookCategory qBookCategory = QBookCategory.bookCategory;

        // QueryDSL로 쿼리 작성
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
                        qBook.publisher,
                        qBook.retailPrice,
                        qBook.isActive
                ))
                .fetch();


        // 총 데이터 수 계산
        long total = from(qBookCategory)
                .join(qBookCategory.book, qBook)
                .where(qBookCategory.category.categoryId.eq(categoryId))
                .fetchCount();

        // Page 객체로 변환하여 반환
        return new PageImpl<>(booksDto, pageable, total);
    }
}
