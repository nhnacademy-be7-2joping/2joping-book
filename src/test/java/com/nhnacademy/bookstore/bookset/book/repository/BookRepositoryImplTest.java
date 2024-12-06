package com.nhnacademy.bookstore.bookset.book.repository;


import com.nhnacademy.bookstore.bookset.book.dto.response.*;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.BookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.BookContributor;
import com.nhnacademy.bookstore.bookset.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.bookset.tag.entity.BookTag;
import com.nhnacademy.bookstore.bookset.tag.entity.Tag;
import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CategoryIdNullException;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static com.nhnacademy.bookstore.bookset.category.entity.QCategory.category;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class BookRepositoryImplTest {

    @Mock
    private BookRepositoryImpl bookRepositoryImpl;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JPAQueryFactory queryFactory;

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

    private ContributorRole createTestContributorRole() {
        ContributorRole contributorRole = new ContributorRole(1L, "지은이");
        return entityManager.merge(contributorRole);

    }

    private Contributor createTestContributor() {
        Contributor contributor = new Contributor(1L, createTestContributorRole(), "Contributor Name", true);
        return entityManager.merge(contributor);
    }

    private Category createTestCategory() {
        Category category = new Category(1L, null, "Test Category", true);
        return entityManager.merge(category);
    }

    private Category createCategory(Long categoryId, Long parentCategoryId, String categoryName) {
        Category parentCategory = parentCategoryId != null ? entityManager.find(Category.class, parentCategoryId) : null;
        Category category = new Category(categoryId, parentCategory, categoryName, true);
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

    @Test
    @DisplayName("존재하지 않는 도서 상세 조회")
    void testFindBookByBookIdWhenNotFound() {
        Long nonExistentBookId = 999L;
        Optional<BookResponseDto> result = bookRepository.findBookByBookId(nonExistentBookId);

        assertFalse(result.isPresent(), "존재하지 않는 도서 조회 시 Optional.empty() 반환");
    }

    @Test
    @DisplayName("카테고리 계층 구조 조회 - 최하위 카테고리 ID를 기준으로")
    void testGetCategoryHierarchy() {
        Category topCategory = createCategory(1L, null, "Top Category");
        Category middleCategory = createCategory(2L, topCategory.getCategoryId(), "Middle Category");
        Category bottomCategory = createCategory(3L, middleCategory.getCategoryId(), "Bottom Category");

        Long lowestCategoryId = bottomCategory.getCategoryId();
        Map<String, Long> result = bookRepository.getCategoryHierarchy(lowestCategoryId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(topCategory.getCategoryId(), result.get("topCategoryId"));
        assertEquals(middleCategory.getCategoryId(), result.get("middleCategoryId"));
        assertEquals(bottomCategory.getCategoryId(), result.get("bottomCategoryId"));
    }

    @Test
    @DisplayName("최상위 카테고리만 있는 경우")
    void testGetCategoryHierarchyWithOnlyTopCategory() {
        Category topCategory = createCategory(1L, null, "Top Category");

        Long lowestCategoryId = topCategory.getCategoryId();
        Map<String, Long> result = bookRepository.getCategoryHierarchy(lowestCategoryId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(topCategory.getCategoryId(), result.get("topCategoryId"));
        assertNull(result.get("middleCategoryId"));
        assertNull(result.get("bottomCategoryId"));
    }

    @Test
    @DisplayName("카테고리 ID가 null인 경우 예외 발생")
    void testGetCategoryHierarchyWithNullCategoryId() {
        assertThrows(CategoryIdNullException.class, () -> bookRepository.getCategoryHierarchy(null));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 ID에 대해 카테고리 계층 구조 조회")
    void testGetCategoryHierarchyWithNonExistentCategory() {
        Long nonExistentCategoryId = 999L;

        Map<String, Long> result = bookRepository.getCategoryHierarchy(nonExistentCategoryId);

        assertNotNull(result);
        assertNull(result.get("topCategoryId"));
        assertNull(result.get("middleCategoryId"));
        assertNull(result.get("bottomCategoryId"));
    }

    @Test
    @DisplayName("도서 수정을 위해 도서 상세 조회")
    void testFindUpdateBookByBookId() {
        Long bookId = 1L;

        Publisher publisher = createTestPublisher();
        Book book = createTestBook(publisher);

        Map<String, Long> categoryHierarchy = new HashMap<>();
        categoryHierarchy.put("topCategoryId", 1L);
        categoryHierarchy.put("middleCategoryId", 2L);
        categoryHierarchy.put("bottomCategoryId", 3L);
        when(bookRepositoryImpl.getCategoryHierarchy(any())).thenReturn(categoryHierarchy);

        Category category = createTestCategory();
        Category middleCategory = new Category(2L, category,"middleCategoryId",true);
        entityManager.merge(middleCategory);

        Category bottomCategory = new Category(3L, middleCategory,"bottomCategoryId",true);
        entityManager.merge(bottomCategory);

        BookCategory.BookCategoryId bookCategoryId = new BookCategory.BookCategoryId(book.getBookId(), bottomCategory.getCategoryId());
        BookCategory bookCategory = new BookCategory(bookCategoryId, book, bottomCategory);
        entityManager.merge(bookCategory);

        ContributorRole contributorRole = createTestContributorRole();
        Contributor contributor1 = new Contributor(1L, contributorRole, "Contributor 1", true);
        entityManager.merge(contributor1);

        BookContributor.BookContributorId bookContributorId = new BookContributor.BookContributorId(book.getBookId(), contributor1.getContributorId());
        BookContributor bookContributor = new BookContributor(bookContributorId, book, contributor1);
        entityManager.merge(bookContributor);

        List<BookContributorResponseDto> contributors = List.of(new BookContributorResponseDto(1L, "Contributor 1", 1L, "지은이"));
        when(bookRepositoryImpl.getContributorsByBook(bookId)).thenReturn(contributors);

        Tag tag1 = new Tag(1L, "Tag 1");
        entityManager.merge(tag1);

        BookTag.BookTagId bookTagId = new BookTag.BookTagId(book.getBookId(), tag1.getTagId());
        BookTag bookTag = new BookTag(bookTagId, book, tag1);
        entityManager.merge(bookTag);

        List<BookTagResponseDto> tags = List.of(new BookTagResponseDto(1L, "Tag 1"));
        when(bookRepositoryImpl.getTagsByBook(bookId)).thenReturn(tags);

        Optional<BookUpdateResponseDto> result = bookRepository.findUpdateBookByBookId(bookId);

        assertTrue(result.isPresent());
        BookUpdateResponseDto dto = result.get();
        assertEquals(book.getBookId(), dto.bookId());
        assertEquals(book.getTitle(), dto.title());
        assertEquals(book.getDescription(), dto.description());
        assertEquals(book.getPublisher().getName(), dto.publisherName());
        assertEquals(book.getPublishedDate(), dto.publishedDate());
        assertEquals(book.getIsbn(), dto.isbn());
        assertEquals(book.getRetailPrice(), dto.retailPrice());
        assertEquals(book.getSellingPrice(), dto.sellingPrice());
        assertTrue(dto.giftWrappable());
        assertTrue(dto.isActive());
        assertEquals(book.getRemainQuantity(), dto.remainQuantity());
        assertEquals(categoryHierarchy.get("topCategoryId"), dto.topCategoryId());
        assertEquals(categoryHierarchy.get("middleCategoryId"), dto.middleCategoryId());
        assertEquals(categoryHierarchy.get("bottomCategoryId"), dto.bottomCategoryId());
        assertEquals(1, dto.contributorList().size());
        assertEquals("Contributor 1", dto.contributorList().getFirst().contributorName());
        assertEquals(1, dto.tagList().size());
        assertEquals("Tag 1", dto.tagList().getFirst().tagName());
        assertEquals("default-thumbnail.jpg", dto.thumbnailImageUrl());
        assertEquals("default-detail.jpg", dto.detailImageUrl());
    }

    @Test
    @DisplayName("책이 존재하지 않을 경우 도서 수정 상세 조회가 빈 값 반환")
    void testFindUpdateBookByBookIdWhenBookNotFound() {
        Long bookId = 999L;

        Map<String, Long> categoryHierarchy = new HashMap<>();
        categoryHierarchy.put("topCategoryId", 1L);
        categoryHierarchy.put("middleCategoryId", 2L);
        categoryHierarchy.put("bottomCategoryId", 3L);
        when(bookRepositoryImpl.getCategoryHierarchy(any())).thenReturn(categoryHierarchy);

        Optional<BookUpdateResponseDto> result = bookRepository.findUpdateBookByBookId(bookId);

        assertFalse(result.isPresent(), "책이 존재하지 않으면 Optional.empty() 반환");
    }
}

