package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.bookset.book.entity.QBookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.QBookContributor;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.QContributor;
import com.nhnacademy.bookstore.bookset.publisher.entity.QPublisher;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.types.Projections.list;

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
        QBookContributor qBookContributor = QBookContributor.bookContributor;
        QContributor qContributor = QContributor.contributor;

        List<Tuple> bookTuples = from(qBook)
                .leftJoin(qBookContributor).on(qBook.bookId.eq(qBookContributor.book.bookId))
                .leftJoin(qContributor).on(qBookContributor.contributor.contributorId.eq(qContributor.contributorId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(qBook, qContributor.name)
                .fetch();

        Map<Long, BookSimpleResponseDto> bookMap = new HashMap<>();
        for (Tuple tuple : bookTuples) {
            Book book = tuple.get(qBook);
            String contributorName = tuple.get(qContributor.name);

            BookSimpleResponseDto dto = bookMap.computeIfAbsent(book.getBookId(), bookId -> new BookSimpleResponseDto(
                    book.getBookId(),
                    "temp.jpg", // 임시 썸네일
                    book.getTitle(),
                    book.getSellingPrice(),
                    book.getPublisher().getName(),
                    book.getRetailPrice(),
                    book.isActive(),
                    new ArrayList<>()
            ));
            if (contributorName != null) {
                dto.contributorList().add(contributorName);
            }
        }

        // 리스트로 변환하여 반환
        List<BookSimpleResponseDto> booksDto = new ArrayList<>(bookMap.values());
        long total = from(qBook).fetchCount();

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
    public Page<BookSimpleResponseDto> findBooksByContributorId(Pageable pageable, Long contributorId) {
        QBook qBook = QBook.book;
        QBookContributor qBookContributor = QBookContributor.bookContributor;
        QContributor qContributor = QContributor.contributor;

        List<Tuple> bookTuples = from(qBookContributor)
                .join(qBookContributor.book, qBook)
                .leftJoin(qBookContributor.contributor, qContributor)
                .where(qBookContributor.contributor.contributorId.eq(contributorId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(qBook, qContributor.name)
                .fetch();

        // 도서 ID 기준으로 기여자들을 매핑
        Map<Long, BookSimpleResponseDto> bookMap = new HashMap<>();
        for (Tuple tuple : bookTuples) {
            Book book = tuple.get(qBook);
            String contributorName = tuple.get(qContributor.name);

            // 이미 책 정보가 있다면 기여자만 추가
            BookSimpleResponseDto dto = bookMap.computeIfAbsent(book.getBookId(), bookId -> new BookSimpleResponseDto(
                    book.getBookId(),
                    "temp.jpg", // 임시 썸네일
                    book.getTitle(),
                    book.getSellingPrice(),
                    book.getPublisher().getName(),
                    book.getRetailPrice(),
                    book.isActive(),
                    new ArrayList<>()
            ));
            if (contributorName != null) {
                dto.contributorList().add(contributorName);
            }
        }

        // 리스트로 변환하여 반환
        List<BookSimpleResponseDto> booksDto = new ArrayList<>(bookMap.values());
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
        QBookContributor qBookContributor = QBookContributor.bookContributor;
        QContributor qContributor = QContributor.contributor;

        List<Tuple> bookTuples = from(qBookCategory)
                .join(qBookCategory.book, qBook)
                .leftJoin(qBookContributor).on(qBook.bookId.eq(qBookContributor.book.bookId))
                .leftJoin(qContributor).on(qBookContributor.contributor.contributorId.eq(qContributor.contributorId))
                .where(qBookCategory.category.categoryId.eq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(qBook, qContributor.name)
                .fetch();

        Map<Long, BookSimpleResponseDto> bookMap = new HashMap<>();
        for (Tuple tuple : bookTuples) {
            Book book = tuple.get(qBook);
            String contributorName = tuple.get(qContributor.name);

            BookSimpleResponseDto dto = bookMap.computeIfAbsent(book.getBookId(), bookId -> new BookSimpleResponseDto(
                    book.getBookId(),
                    "temp.jpg", // 임시 썸네일
                    book.getTitle(),
                    book.getSellingPrice(),
                    book.getPublisher().getName(),
                    book.getRetailPrice(),
                    book.isActive(),
                    new ArrayList<>()
            ));
            if (contributorName != null) {
                dto.contributorList().add(contributorName);
            }
        }

        // 리스트로 변환하여 반환
        List<BookSimpleResponseDto> booksDto = new ArrayList<>(bookMap.values());
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
        QBookContributor qBookContributor = QBookContributor.bookContributor;
        QContributor qContributor = QContributor.contributor;

        // 도서와 기여자 정보를 함께 조회
        List<Tuple> bookTuples = from(qBook)
                .leftJoin(qBookContributor).on(qBook.bookId.eq(qBookContributor.book.bookId))
                .leftJoin(qContributor).on(qBookContributor.contributor.contributorId.eq(qContributor.contributorId))
                .where(qBook.bookId.eq(bookId))
                .select(qBook, qContributor.name)
                .fetch();

        // 도서 정보를 담기 위한 변수 초기화
        BookResponseDto bookResponseDto = null;
        List<String> contributorNames = new ArrayList<>();

        // 튜플을 반복하며 도서와 기여자 정보를 수집
        for (Tuple tuple : bookTuples) {
            Book book = tuple.get(qBook);
            String contributorName = tuple.get(qContributor.name);

            if (bookResponseDto == null) {
                // 최초로 Book 정보를 설정 (한번만 설정)
                bookResponseDto = new BookResponseDto(
                        book.getBookId(),
                        book.getPublisher().getName(),
                        book.getTitle(),
                        book.getDescription(),
                        book.getPublishedDate(),
                        book.getIsbn(),
                        book.getRetailPrice(),
                        book.getSellingPrice(),
                        book.isGiftWrappable(),
                        book.isActive(),
                        book.getRemainQuantity(),
                        book.getViews(),
                        book.getLikes(),
                        contributorNames,
                        "temp.jpg" // 임시 썸네일
                );
            }

            // 기여자 이름을 리스트에 추가
            if (contributorName != null) {
                contributorNames.add(contributorName);
            }
        }

        return bookResponseDto;
    }

}


