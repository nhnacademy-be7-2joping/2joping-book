package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.bookset.book.entity.QBookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.QBookContributor;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

@Slf4j
public class BookRepositoryImpl extends QuerydslRepositorySupport implements BookRepositoryCustom {

    public BookRepositoryImpl() {
        super(Book.class);
    }

    @PersistenceContext
    private EntityManager entityManager;
    /**
     * 기여자로 도서를 조회하는 메서드
     * @param contributorId 검색하려는 기여자id
     * @return 기여자별 도서와 상태 코드를 담은 응답
     */
//    @Override
//    public Page<BookSimpleResponseDto> findBooksByContributorId(Pageable pageable,Long contributorId) {
//        QBook qBook = QBook.book;
//        QBookContributor qBookContributor = QBookContributor.bookContributor;
//
//        // QueryDSL로 쿼리 작성
//        List<BookSimpleResponseDto> booksDto = from(qBookContributor)
//                .join(qBookContributor.book, qBook)
//                .where(qBookContributor.contributor.contributorId.eq(contributorId))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .select(Projections.constructor(
//                        BookSimpleResponseDto.class,
//                        qBook.bookId,
//                        Expressions.constant("temp.jpg"), // 썸네일 경로가 임시일 경우
//                        qBook.title,
//                        qBook.sellingPrice,
//                        qBook.publisher,
//                        qBook.retailPrice,
//                        qBook.isActive
//                ))
//                .fetch();
//
//        // 총 데이터 수 계산
//        long total = from(qBookContributor)
//                .join(qBookContributor.book, qBook)
//                .where(qBookContributor.contributor.contributorId.eq(contributorId))
//                .distinct()
//                .fetchCount();
//
//        // Page 객체로 변환하여 반환
//        return new PageImpl<>(booksDto, pageable, total);
//    }
    @Override
    public Page<BookSimpleResponseDto> findBooksByContributorId(Pageable pageable, Long contributorId) {
        QBook qBook = QBook.book;
        QBookContributor qBookContributor = QBookContributor.bookContributor;

        // 페이징된 데이터를 가져오는 쿼리
        List<BookSimpleResponseDto> booksDto = from(qBookContributor)
                .join(qBookContributor.book, qBook)
                .where(qBookContributor.contributor.contributorId.eq(contributorId))
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

        long total = new JPAQuery<>(entityManager)
                .from(qBookContributor)
                .join(qBookContributor.book, qBook)
                .where(qBookContributor.contributor.contributorId.eq(contributorId))
                .fetchCount();

        // Page 객체로 반환
        return new PageImpl<>(booksDto, pageable, total);
    }


    /**
     * 카테고리로 도서를 조회하는 메서드
     * @param categoryId 검색하려는 카테고리id
     * @return 카테고리별 도서와 상태 코드를 담은 응답
     */
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
                .distinct()
                .fetchCount();

        // Page 객체로 변환하여 반환
        return new PageImpl<>(booksDto, pageable, total);
    }



    /**
     * 책 하나의 상세한 정보를 조회하는 메서드
     * @param bookId 검색하려는 도서id
     * @return 특정 도서와 상태 코드를 담은 응답
     */
    @Override
    public Optional<Book> findBookByBookId(Long bookId) {
        QBook qBook = QBook.book;

        Book book = from(qBook)
                .where(qBook.bookId.eq(bookId))
                .select(qBook)
                .fetchOne();

        return Optional.ofNullable(book);
    }
}


