package com.nhnacademy.bookstore.bookset.book.repository;


import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.BookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.BookContributor;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class) // QueryDSL 설정 포함
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 데이터베이스 사용
@ActiveProfiles("test") // 테스트 프로파일 활성화
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // 각 테스트 후 컨텍스트 리셋
public class BookRepositoryImplTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Publisher createTestPublisher() {
        Publisher publisher = new Publisher(1L, "Test Publisher");
        return entityManager.merge(publisher);
    }

    private Book createTestBook(Publisher publisher) {
        Book book = new Book(
                1L,
                publisher,
                "Test Book Title",
                "Test description",
                LocalDate.of(2023, 1, 1),
                "9781234567890",
                20000,
                18000,
                true,
                true,
                100,
                50,
                10,
                new ArrayList<>()
        );
        return entityManager.merge(book);
    }

    private ContributorRole createTestContributorRole(){
        ContributorRole contributorRole = new ContributorRole(1L,"지은이");
        return entityManager.merge(contributorRole);

    }
    private Contributor createTestContributor() {
        Contributor contributor = new Contributor(1L, createTestContributorRole(),"Contributor Name",true);
        return entityManager.merge(contributor);
    }

    private Category createTestCategory() {
        Category category = new Category(1L, null,"Test Category",true);
        return entityManager.merge(category);
    }

    @Test
    @DisplayName("전체 도서 조회 - 페이징")
    void testFindAllBooks() {
        Publisher publisher = createTestPublisher();
        createTestBook(publisher);

        Pageable pageable = PageRequest.of(0, 10);

        Page<BookSimpleResponseDto> result = bookRepository.findAllBooks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book Title", result.getContent().get(0).title());
    }

    @Test
    @DisplayName("특정 기여자가 참여한 도서 조회 - 페이징")
    void testFindBooksByContributorId() {
        Publisher publisher = createTestPublisher();
        Book book = createTestBook(publisher);
        Contributor contributor = createTestContributor();

        // BookContributor 데이터 추가
        BookContributor.BookContributorId bookContributorId = new BookContributor.BookContributorId(book.getBookId(), contributor.getContributorId());
        BookContributor bookContributor = new BookContributor(bookContributorId, book, contributor);
        entityManager.merge(bookContributor);


        Pageable pageable = PageRequest.of(0, 10);
        Page<BookSimpleResponseDto> result = bookRepository.findBooksByContributorId(pageable, contributor.getContributorId());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book Title", result.getContent().get(0).title());
    }

    @Test
    @DisplayName("특정 카테고리에 속한 도서 조회 - 페이징")
    void testFindBooksByCategoryId() {
        Publisher publisher = createTestPublisher();
        Book book = createTestBook(publisher);
        Category category = createTestCategory();

        // BookCategory 데이터 추가
        BookCategory.BookCategoryId bookCategoryId = new BookCategory.BookCategoryId(book.getBookId(), category.getCategoryId());
        BookCategory bookCategory = new BookCategory(bookCategoryId, book, category);
        entityManager.merge(bookCategory);

        Pageable pageable = PageRequest.of(0, 10);
        Page<BookSimpleResponseDto> result = bookRepository.findBooksByCategoryId(pageable, category.getCategoryId());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book Title", result.getContent().get(0).title());
    }

    @Test
    @DisplayName("특정 도서 상세 조회")
    void testFindBookByBookId() {
        Publisher publisher = createTestPublisher();
        Book book = createTestBook(publisher);

        Optional<BookResponseDto> result = bookRepository.findBookByBookId(book.getBookId());

        assertTrue(result.isPresent());
        assertEquals("Test Book Title", result.get().title());
    }
}

