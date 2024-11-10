package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.dto.response.BookContributorResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.bookset.book.entity.QBookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.QBookContributor;
import com.nhnacademy.bookstore.bookset.category.entity.QCategory;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.QContributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.QContributorRole;
import com.querydsl.core.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.*;


@Slf4j
public class BookRepositoryImpl extends QuerydslRepositorySupport implements BookRepositoryCustom {

    public BookRepositoryImpl() {
        super(Book.class);
    }

    private final QBook qBook = QBook.book;
    private final QBookContributor qBookContributor = QBookContributor.bookContributor;
    private final QContributor qContributor = QContributor.contributor;
    private final QBookCategory qBookCategory = QBookCategory.bookCategory;
    private final QCategory qCategory = QCategory.category;
    private final QContributorRole qContributorRole = QContributorRole.contributorRole;

    /**
     * 전체 도서를 페이지 단위로 조회
     *
     * @param pageable 페이징 정보를 담고 있는 객체
     * @return 전체 도서 목록과 페이징 정보를 담은 Page 객체
     */
    @Override
    public Page<BookSimpleResponseDto> findAllBooks(Pageable pageable) {

        List<Tuple> bookTuples = from(qBook)
                .leftJoin(qBookContributor).on(qBook.bookId.eq(qBookContributor.book.bookId))
                .leftJoin(qContributor).on(qBookContributor.contributor.contributorId.eq(qContributor.contributorId))
                .leftJoin(qContributorRole).on(qContributor.contributorRole.contributorRoleId.eq(qContributorRole.contributorRoleId))
                .leftJoin(qBookCategory).on(qBook.bookId.eq(qBookCategory.book.bookId))
                .leftJoin(qCategory).on(qBookCategory.category.categoryId.eq(qCategory.categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(qBook, qContributor, qContributorRole.name, qCategory.category.name)
                .fetch();

        Map<Long, BookSimpleResponseDto> bookMap = new HashMap<>();

        for (Tuple tuple : bookTuples) {
            Book book = tuple.get(qBook);
            Contributor contributor = tuple.get(qContributor);
            String contributorRoleName = tuple.get(qContributorRole.name);
            String categoryName = tuple.get(qCategory.category.name);

            BookSimpleResponseDto existingDto = bookMap.get(book.getBookId());

            if (existingDto == null) {
                // DTO가 처음 생성되는 경우
                existingDto = new BookSimpleResponseDto(
                        book.getBookId(),
                        "temp.jpg", // 임시 썸네일
                        book.getTitle(),
                        book.getSellingPrice(),
                        book.getPublisher().getName(),
                        book.getRetailPrice(),
                        book.isActive(),
                        new ArrayList<>(), // 기여자 리스트 초기화
                        new ArrayList<>() // 카테고리 리스트 빈 리스트로 초기화
                );
                bookMap.put(book.getBookId(), existingDto);
            }

            // 기여자 리스트 추가
            if (contributor != null && contributor.getContributorId() != null) {
                BookContributorResponseDto contributorDto = new BookContributorResponseDto(
                        contributor.getContributorId(),
                        contributor.getName(),
                        contributor.getContributorRole().getContributorRoleId(),
                        contributorRoleName
                );
                if (!existingDto.contributorList().contains(contributorDto)) {
                    existingDto.contributorList().add(contributorDto);
                }
            }

            // 카테고리 리스트 추가
            if (categoryName != null && !existingDto.categoryList().contains(categoryName)) {
                existingDto.categoryList().add(categoryName);
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

        // 특정 기여자가 참여한 도서를 조회
        List<Tuple> bookTuples = from(qBookContributor)
                .join(qBookContributor.book, qBook)
                .leftJoin(qBookCategory).on(qBook.bookId.eq(qBookCategory.book.bookId))
                .leftJoin(qCategory).on(qBookCategory.category.categoryId.eq(qCategory.categoryId))
                .leftJoin(qContributor).on(qBookContributor.contributor.contributorId.eq(qContributor.contributorId))
                .leftJoin(qContributorRole).on(qContributor.contributorRole.contributorRoleId.eq(qContributorRole.contributorRoleId))
                .where(qBookContributor.contributor.contributorId.eq(contributorId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(qBook, qContributor, qContributorRole.name, qCategory.category.name)
                .fetch();

        // 도서 ID를 기준으로 BookSimpleResponseDto를 수집하기 위한 맵 초기화
        Map<Long, BookSimpleResponseDto> bookMap = new HashMap<>();

        for (Tuple tuple : bookTuples) {
            Book book = tuple.get(qBook);
            Contributor contributor = tuple.get(qContributor);
            String contributorRoleName = tuple.get(qContributorRole.name);
            String categoryName = tuple.get(qCategory.category.name);

            // 맵에서 도서 정보를 가져오거나 새로 생성
            BookSimpleResponseDto existingDto = bookMap.get(book.getBookId());

            if (existingDto == null) {
                existingDto = new BookSimpleResponseDto(
                        book.getBookId(),
                        "temp.jpg", // 임시 썸네일
                        book.getTitle(),
                        book.getSellingPrice(),
                        book.getPublisher().getName(),
                        book.getRetailPrice(),
                        book.isActive(),
                        new ArrayList<>(), // 기여자 리스트 초기화
                        new ArrayList<>() // 카테고리 리스트 빈 리스트로 초기화
                );
                bookMap.put(book.getBookId(), existingDto);
            }

            // 기여자 리스트 추가
            if (contributor != null && contributor.getContributorId() != null) {
                BookContributorResponseDto contributorDto = new BookContributorResponseDto(
                        contributor.getContributorId(),
                        contributor.getName(),
                        contributor.getContributorRole().getContributorRoleId(),
                        contributorRoleName
                );
                if (!existingDto.contributorList().contains(contributorDto)) {
                    existingDto.contributorList().add(contributorDto);
                }
            }

            // 카테고리 리스트 추가
            if (categoryName != null && !existingDto.categoryList().contains(categoryName)) {
                existingDto.categoryList().add(categoryName);
            }
        }

        // 리스트로 변환하여 반환
        List<BookSimpleResponseDto> booksDto = new ArrayList<>(bookMap.values());
        long total = from(qBookContributor)
                .where(qBookContributor.contributor.contributorId.eq(contributorId))
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

        // 특정 카테고리에 속하는 도서를 조회
        List<Tuple> bookTuples = from(qBookCategory)
                .join(qBookCategory.book, qBook)
                .leftJoin(qBookContributor).on(qBook.bookId.eq(qBookContributor.book.bookId))
                .leftJoin(qContributor).on(qBookContributor.contributor.contributorId.eq(qContributor.contributorId))
                .leftJoin(qContributorRole).on(qContributor.contributorRole.contributorRoleId.eq(qContributorRole.contributorRoleId))
                .leftJoin(qCategory).on(qBookCategory.category.categoryId.eq(qCategory.categoryId))
                .where(qBookCategory.category.categoryId.eq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(qBook, qContributor, qContributorRole.name, qCategory.category.name)
                .fetch();

        // 도서 ID를 기준으로 BookSimpleResponseDto를 수집하기 위한 맵 초기화
        Map<Long, BookSimpleResponseDto> bookMap = new HashMap<>();

        for (Tuple tuple : bookTuples) {
            Book book = tuple.get(qBook);
            Contributor contributor = tuple.get(qContributor);
            String contributorRoleName = tuple.get(qContributorRole.name);
            String categoryName = tuple.get(qCategory.category.name);

            // 맵에서 도서 정보를 가져오거나 새로 생성
            BookSimpleResponseDto existingDto = bookMap.get(book.getBookId());

            if (existingDto == null) {
                existingDto = new BookSimpleResponseDto(
                        book.getBookId(),
                        "temp.jpg", // 임시 썸네일
                        book.getTitle(),
                        book.getSellingPrice(),
                        book.getPublisher().getName(),
                        book.getRetailPrice(),
                        book.isActive(),
                        new ArrayList<>(), // 기여자 리스트 초기화
                        new ArrayList<>() // 카테고리 리스트 빈 리스트로 초기화
                );
                bookMap.put(book.getBookId(), existingDto);
            }

            // 기여자 리스트 추가
            if (contributor != null && contributor.getContributorId() != null) {
                BookContributorResponseDto contributorDto = new BookContributorResponseDto(
                        contributor.getContributorId(),
                        contributor.getName(),
                        contributor.getContributorRole().getContributorRoleId(),
                        contributorRoleName
                );
                if (!existingDto.contributorList().contains(contributorDto)) {
                    existingDto.contributorList().add(contributorDto);
                }
            }

            // 카테고리 리스트 추가
            if (categoryName != null && !existingDto.categoryList().contains(categoryName)) {
                existingDto.categoryList().add(categoryName);
            }
        }

        // 리스트로 변환하여 반환
        List<BookSimpleResponseDto> booksDto = new ArrayList<>(bookMap.values());
        long total = from(qBookCategory)
                .where(qBookCategory.category.categoryId.eq(categoryId))
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
    public Optional<BookResponseDto> findBookByBookId(Long bookId) {

        // 도서와 기여자 및 카테고리 정보를 함께 조회
        List<Tuple> bookTuples = from(qBook)
                .leftJoin(qBookContributor).on(qBook.bookId.eq(qBookContributor.book.bookId))
                .leftJoin(qContributor).on(qBookContributor.contributor.contributorId.eq(qContributor.contributorId))
                .leftJoin(qContributorRole).on(qContributor.contributorRole.contributorRoleId.eq(qContributorRole.contributorRoleId))
                .leftJoin(qBookCategory).on(qBook.bookId.eq(qBookCategory.book.bookId))
                .leftJoin(qCategory).on(qBookCategory.category.categoryId.eq(qCategory.categoryId))
                .where(qBook.bookId.eq(bookId))
                .select(qBook, qContributor, qContributorRole.name, qCategory.category.name)
                .fetch();

        // 도서 정보를 담기 위한 변수 초기화
        BookResponseDto bookResponseDto = null;
        List<BookContributorResponseDto> contributorDtos = new ArrayList<>();
        List<String> categoryNames = new ArrayList<>();

        // 튜플을 반복하며 도서, 기여자 및 카테고리 정보를 수집
        for (Tuple tuple : bookTuples) {
            Book book = tuple.get(qBook);
            Contributor contributor = tuple.get(qContributor);
            String contributorRoleName = tuple.get(qContributorRole.name);
            String categoryName = tuple.get(qCategory.category.name);

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
                        contributorDtos,
                        categoryNames,
                        "temp.jpg" // 임시 썸네일
                );
            }

            // 기여자 리스트 추가
            if (contributor != null && contributor.getContributorId() != null) {
                BookContributorResponseDto contributorDto = new BookContributorResponseDto(
                        contributor.getContributorId(),
                        contributor.getName(),
                        contributor.getContributorRole().getContributorRoleId(),
                        contributorRoleName
                );
                if (!contributorDtos.contains(contributorDto)) {
                    contributorDtos.add(contributorDto);
                }
            }

            // 카테고리 이름을 리스트에 추가
            if (categoryName != null && !categoryNames.contains(categoryName)) {
                categoryNames.add(categoryName);
            }
        }
        return Optional.ofNullable(bookResponseDto);
    }
}


