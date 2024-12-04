package com.nhnacademy.bookstore.bookset.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.nhnacademy.bookstore.bookset.book.dto.request.*;
import com.nhnacademy.bookstore.bookset.book.dto.response.*;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookContributorRepository;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRepository;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.bookset.book.repository.BookCategoryRepository;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.entity.Tag;
import com.nhnacademy.bookstore.bookset.tag.repository.BookTagRepository;
import com.nhnacademy.bookstore.bookset.tag.repository.TagRepository;
import com.nhnacademy.bookstore.common.error.exception.bookset.book.BookNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CategoryNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.publisher.PublisherNotFoundException;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstore.common.error.exception.bookset.tag.TagNotFoundException;
import com.nhnacademy.bookstore.imageset.entity.BookImage;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.repository.BookImageRepository;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookServiceImplTest {

    @Spy
    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private ContributorRepository contributorRepository;
    @Mock
    private ContributorRoleRepository contributorRoleRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;
    // book~repository 필요
    @Mock
    private BookContributorRepository bookContributorRepository;
    @Mock
    private BookCategoryRepository bookCategoryRepository;
    @Mock
    private BookTagRepository bookTagRepository;
    @Mock
    private BookImageRepository bookImageRepository;

    private BookCreateRequestDto bookCreateRequestDto;
    private BookSimpleResponseDto bookSimpleDto;
    private BookResponseDto bookResponseDto;
    private Page<BookSimpleResponseDto> bookPage;
    private List<BookContributorResponseDto> contributors;
    private BookUpdateRequestDto bookUpdateRequestDto;
    private BookUpdateResponseDto bookUpdateResponseDto;

    @BeforeEach
    void setUp() {
        bookCreateRequestDto = new BookCreateRequestDto(
                new BookCreateHtmlRequestDto(
                        "Book Title",
                        "Description",
                        "경영출판사",
                        LocalDate.of(2024, 11, 1),
                        "1234567890123",
                        30000,
                        25000,
                        true,
                        true,
                        15,
                        "[{\"name\":\"홍길동\",\"role\":\"지은이\"}]",
                        1L, 2L, 3L, List.of("따뜻한")
                ),
                new ImageUrlRequestDto("thumbnail-url", "detail-url")
        );

        bookUpdateRequestDto = new BookUpdateRequestDto(
                new BookUpdateHtmlRequestDto("Updated Title", "Updated Description", "수정출판사", LocalDate.of(2024, 1, 1),
                "1234567890123", 30000, 25000, true, true, 10,
                "[{\"name\":\"홍길동\",\"role\":\"지은이\"}]", 1L, 2L, 3L,
                List.of("수정된 태그"), true, true),
                new ImageUrlRequestDto("new-thumbnail-url", "new-detail-url")
        );

        bookUpdateResponseDto = new BookUpdateResponseDto(
                1L, "테스트 도서 제목", "테스트 도서 설명", "테스트 출판사", LocalDate.of(2023, 12, 1),
                "9791194330424", 30000, 27000, true, true, 100,
                List.of(
                        new BookContributorResponseDto(1L, "저자 이름", 1L, "지은이")
                ),
                1L, 2L, 3L,
                List.of(
                        new BookTagResponseDto(1L, "태그1"),
                        new BookTagResponseDto(2L, "태그2")
                ),
                "https://example.com/thumbnail.jpg",
                "https://example.com/detail.jpg",
                false,
                false
        );

        contributors = List.of(
                new BookContributorResponseDto(1L, "Contributor 1", 1L, "Author")
        );

        bookSimpleDto = new BookSimpleResponseDto(
                1L, "thumbnail1", "Book Title 1", 15000, "Publisher 1", 20000, true,
                contributors, List.of("Category 1", "Category 2")
        );

        bookPage = new PageImpl<>(List.of(bookSimpleDto));

        bookResponseDto = new BookResponseDto(
                1L, "Publisher 1", "Book Title 1", "Description", LocalDate.of(2023, 10, 29),
                "1234567890123", 20000, 15000, true, true, 10, 0, 0,
                contributors, List.of("Category 1", "Category 2"), List.of(new BookTagResponseDto(1L,"Tag 1")),"thumbnail1",List.of(new ReviewResponseDto(1L,1L,1L,1L,5,"제목","내용","이미지", Timestamp.valueOf(LocalDateTime.now()),null))
        );
    }

    // ---- Helper Methods ----
    private Publisher createPublisher(String name) {
        return new Publisher(1L, name);
    }

    private Category createCategory(Long id, Category parent, String name) {
        return new Category(id, parent, name, true);
    }

    private ContributorRole createContributorRole(Long id, String name) {
        return new ContributorRole(id, name);
    }

    private Contributor createContributor(Long id, ContributorRole role, String name) {
        return new Contributor(id, role, name, true);
    }

    private Book createMockBook(Long bookId, String title, String publisherName) {
        Publisher publisher = new Publisher(1L, publisherName);
        return new Book(bookId, publisher, title, "Old Description", LocalDate.of(2020, 1, 1), "9876543210987",
                20000, 18000, true, true, 5, 0, 0, null);
    }

    private BookUpdateRequestDto createMockUpdateRequestDto(
            boolean removeThumbnailImage,
            boolean removeDetailImage,
            String thumbnailImageUrl,
            String detailImageUrl
    ) {
        BookUpdateHtmlRequestDto htmlDto = new BookUpdateHtmlRequestDto("Updated Title", "Updated Description", "수정출판사", LocalDate.of(2024, 1, 1),
                "1234567890123", 30000, 25000, true, true, 10,
                "[{\"name\":\"홍길동\",\"role\":\"지은이\"}]", 1L, 2L, 3L,
                List.of("수정된 태그"), removeThumbnailImage, removeDetailImage);
        ImageUrlRequestDto imageUrlDto = new ImageUrlRequestDto(thumbnailImageUrl, detailImageUrl);
        return new BookUpdateRequestDto(htmlDto, imageUrlDto);
    }

    private BookCreateRequestDto createMockBookCreateRequestDto(
            String thumbnailImageUrl, String detailImageUrl
    ) {
        BookCreateHtmlRequestDto htmlRequestDto = new BookCreateHtmlRequestDto("Book Title", "Description",
                "경영출판사", LocalDate.of(2024, 11, 1), "1234567890123",
                30000, 25000, true, true, 15, "[{\"name\":\"홍길동\",\"role\":\"지은이\"}]",
                1L, 2L, 3L, List.of("따뜻한"));
        ImageUrlRequestDto imageUrlRequestDto = new ImageUrlRequestDto(thumbnailImageUrl, detailImageUrl);
        return new BookCreateRequestDto(htmlRequestDto, imageUrlRequestDto);
    }

    // ---- Test Cases ----
    @Test
    @DisplayName("도서 단독 등록 성공")
    void testCreateBookSuccess() {
        when(publisherRepository.findByName("경영출판사")).thenReturn(Optional.of(createPublisher("경영출판사")));

        ContributorRole role = createContributorRole(1L, "지은이");
        Contributor contributor = createContributor(1L, role, "홍길동");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(role));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(createCategory(3L, null, "현대소설")));

        Tag warmTag = new Tag(1L, "따뜻한");
        when(tagRepository.findByName("따뜻한")).thenReturn(Optional.of(warmTag));

        Book book = new Book(null, createPublisher("경영출판사"), "Book Title", "Description",
                LocalDate.of(2024, 11, 1), "1234567890123", 30000, 25000, true, true, 15, 0, 0, null);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> {
            Image image = invocation.getArgument(0);
            return new Image(image.getUrl());
        });

        BookCreateResponseDto response = bookService.createBook(createMockBookCreateRequestDto("thumbnail-url", "detail-url"));

        assertNotNull(response);
        assertEquals("Book Title", response.title());
        verify(publisherRepository).findByName("경영출판사");
        verify(bookRepository).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository, publisherRepository);
    }

    @Test
    @DisplayName("도서 단독 등록 성공 - 기본 이미지")
    void testCreateBookSuccessWithDefaultImages() {
        when(publisherRepository.findByName("경영출판사")).thenReturn(Optional.of(createPublisher("경영출판사")));

        ContributorRole role = createContributorRole(1L, "지은이");
        Contributor contributor = createContributor(1L, role, "홍길동");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(role));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(createCategory(3L, null, "현대소설")));

        Tag warmTag = new Tag(1L, "따뜻한");
        when(tagRepository.findByName("따뜻한")).thenReturn(Optional.of(warmTag));

        Book book = new Book(null, createPublisher("경영출판사"), "Book Title", "Description",
                LocalDate.of(2024, 11, 1), "1234567890123", 30000, 25000, true, true, 15, 0, 0, null);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookCreateResponseDto response = bookService.createBook(createMockBookCreateRequestDto(null, null));

        assertNotNull(response);
        assertEquals("Book Title", response.title());
        verify(publisherRepository).findByName("경영출판사");
        verify(bookRepository).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository, publisherRepository);
    }

    @Test
    @DisplayName("도서 단독 등록 실패 - 출판사 없음")
    void testCreateBookFail_PublisherNotFound() {
        when(publisherRepository.findByName("경영출판사")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> bookService.createBook(bookCreateRequestDto));

        verify(publisherRepository, times(1)).findByName("경영출판사");
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("도서 단독 등록 실패 - 카테고리 없음")
    void testCreateBookFail_CategoryNotFound() {
        when(publisherRepository.findByName("경영출판사")).thenReturn(Optional.of(createPublisher("경영출판사")));

        ContributorRole role = createContributorRole(1L, "지은이");
        Contributor contributor = createContributor(1L, role, "홍길동");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(role));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));

        when(categoryRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> bookService.createBook(bookCreateRequestDto));

        verify(publisherRepository, times(1)).findByName("경영출판사");
        verify(categoryRepository, times(1)).findById(3L);
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("도서 단독 등록 실패 - 기여자 역할 없음")
    void testCreateBookFail_ContributorRoleNotFound() {
        when(publisherRepository.findByName("경영출판사")).thenReturn(Optional.of(createPublisher("경영출판사")));
        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.empty());

        assertThrows(ContributorRoleNotFoundException.class, () -> bookService.createBook(bookCreateRequestDto));

        verify(publisherRepository, times(1)).findByName("경영출판사");
        verify(contributorRoleRepository, times(1)).findByName("지은이");
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("도서 단독 등록 실패 - 기여자 없음")
    void testCreateBookFail_ContributorNotFound() {
        when(publisherRepository.findByName("경영출판사")).thenReturn(Optional.of(createPublisher("경영출판사")));

        ContributorRole roleAuthor = new ContributorRole(1L, "지은이");
        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(roleAuthor));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.empty());

        assertThrows(ContributorNotFoundException.class, () -> bookService.createBook(bookCreateRequestDto));

        verify(publisherRepository, times(1)).findByName("경영출판사");
        verify(contributorRoleRepository, times(1)).findByName("지은이");
        verify(contributorRepository, times(1)).findByName("홍길동");
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("도서 단독 등록 실패 - 태그 없음")
    void testCreateBookFail_TagNotFound() {
        when(publisherRepository.findByName("경영출판사")).thenReturn(Optional.of(createPublisher("경영출판사")));

        ContributorRole role = createContributorRole(1L, "지은이");
        Contributor contributor = createContributor(1L, role, "홍길동");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(role));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(createCategory(3L, null, "현대소설")));

        when(tagRepository.findByName("따뜻한")).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> bookService.createBook(bookCreateRequestDto));

        verify(publisherRepository, times(1)).findByName("경영출판사");
        verify(contributorRoleRepository, times(1)).findByName("지은이");
        verify(contributorRepository, times(1)).findByName("홍길동");
        verify(tagRepository, times(1)).findByName("따뜻한");
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBooks_shouldProcessFirstResult() throws Exception {
        String query = "history";

        JsonNode mockRootNode = mock(JsonNode.class);
        ArrayNode mockItemsNode = mock(ArrayNode.class);
        JsonNode mockItemNode = mock(JsonNode.class);
        Image mockImage = new Image("https://image.aladin.co.kr/product/34917/23/coversum/k022933254_1.jpg");
        Contributor mockContributor = createContributor(1L, new ContributorRole(1L, "지은이"), "유발 하라리");

        when(objectMapper.readTree(anyString())).thenReturn(mockRootNode);
        when(mockRootNode.path("item")).thenReturn(mockItemsNode);
        when(mockItemsNode.iterator()).thenReturn(Collections.singleton(mockItemNode).iterator());

        when(mockItemNode.path("title")).thenReturn(new TextNode("넥서스 - 석기시대부터 AI까지"));
        when(mockItemNode.path("publisher")).thenReturn(new TextNode("김영사"));
        when(mockItemNode.path("pubDate")).thenReturn(new TextNode("2024-10-11"));
        when(mockItemNode.path("isbn13")).thenReturn(new TextNode("9791194330424"));
        when(mockItemNode.path("priceStandard")).thenReturn(new IntNode(27800));
        when(mockItemNode.path("priceSales")).thenReturn(new IntNode(25020));
        when(mockItemNode.path("description")).thenReturn(new TextNode("《사피엔스》 논지가 정보로 통합된 《넥서스》."));
        when(mockItemNode.path("author")).thenReturn(new TextNode("유발 하라리"));
        when(mockItemNode.path("categoryName")).thenReturn(new TextNode("국내도서>인문학>문화/문화이론>문명/문명사"));
        when(mockItemNode.path("cover")).thenReturn(new TextNode("https://image.aladin.co.kr/product/34917/23/coversum/k022933254_1.jpg"));

        when(categoryRepository.findByName("국내도서")).thenReturn(Optional.of(createCategory(1L, null, "국내도서")));
        when(categoryRepository.findByName("인문학")).thenReturn(Optional.of(createCategory(2L, null, "인문학")));
        when(categoryRepository.findByName("문화/문화이론")).thenReturn(Optional.of(createCategory(3L, null, "문화/문화이론")));
        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);
        when(publisherRepository.findByName("김영사")).thenReturn(Optional.of(createPublisher("김영사")));
        when(contributorRepository.findByName("유발 하라리")).thenReturn(Optional.of(mockContributor));

        ContributorResponseDto mockContributorResonseDto = new ContributorResponseDto(1L, 1L, "유발 하라리");
        doReturn(List.of(mockContributorResonseDto))
                .when(bookService).getContributorListForAPI(anyString());

        when(contributorRepository.findById(1L)).thenReturn(Optional.of(mockContributor));

        List<BookCreateAPIResponseDto> result = bookService.createBooks(query);

        assertEquals(1, result.size());
        BookCreateAPIResponseDto responseDto = result.get(0);
        assertEquals("넥서스 - 석기시대부터 AI까지", responseDto.title());
        assertEquals("김영사", responseDto.publisherName());
        assertEquals("《사피엔스》 논지가 정보로 통합된 《넥서스》.", responseDto.description());
        assertEquals(LocalDate.of(2024, 10, 11), responseDto.publishedDate());
        assertEquals("9791194330424", responseDto.isbn());
        assertEquals(27800, responseDto.retailPrice());
        assertEquals(25020, responseDto.sellingPrice());
        assertEquals("https://image.aladin.co.kr/product/34917/23/coversum/k022933254_1.jpg", responseDto.thumbnail());
    }

    @Test
    void createBooks_shouldThrowExceptionWhenApiCallFails() throws JsonProcessingException {
        String query = "history";

        JsonNode mockRootNode = mock(JsonNode.class);
        ArrayNode mockItemsNode = mock(ArrayNode.class);
        JsonNode mockItemNode = mock(JsonNode.class);
        Image mockImage = new Image("https://image.aladin.co.kr/product/34917/23/coversum/k022933254_1.jpg");

        when(objectMapper.readTree(anyString())).thenReturn(mockRootNode);
        when(mockRootNode.path("item")).thenReturn(mockItemsNode);
        when(mockItemsNode.iterator()).thenReturn(Collections.singleton(mockItemNode).iterator());

        when(mockItemNode.path("title")).thenReturn(new TextNode("넥서스 - 석기시대부터 AI까지"));
        when(mockItemNode.path("publisher")).thenReturn(new TextNode("김영사"));
        when(mockItemNode.path("pubDate")).thenReturn(new TextNode("2024-10-11"));
        when(mockItemNode.path("isbn13")).thenReturn(new TextNode("9791194330424"));
        when(mockItemNode.path("priceStandard")).thenReturn(new IntNode(27800));
        when(mockItemNode.path("priceSales")).thenReturn(new IntNode(25020));
        when(mockItemNode.path("description")).thenReturn(new TextNode("《사피엔스》 논지가 정보로 통합된 《넥서스》."));
        when(mockItemNode.path("author")).thenReturn(new TextNode("유발 하라리"));
        when(mockItemNode.path("categoryName")).thenReturn(new TextNode("국내도서>인문학>문화/문화이론>문명/문명사"));
        when(mockItemNode.path("cover")).thenReturn(new TextNode("https://image.aladin.co.kr/product/34917/23/coversum/k022933254_1.jpg"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.createBooks(query));
        assertEquals("데이터 처리 중 예외 발생", exception.getMessage());
    }

    @Test
    @DisplayName("전체 도서 조회")
    void testGetAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findAllBooks(any(Pageable.class))).thenReturn(bookPage);

        Page<BookSimpleResponseDto> result = bookService.getAllBooks(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(bookSimpleDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("카테고리 ID로 도서 조회")
    void testGetBooksByCategoryId() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findBooksByCategoryId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        Page<BookSimpleResponseDto> result = bookService.getBooksByCategoryId(pageable, 1L);

        assertEquals(1, result.getTotalElements());
        assertEquals(bookSimpleDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("기여자 ID로 도서 조회")
    void testGetBooksByContributorId() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findBooksByContributorId(any(Pageable.class), anyLong())).thenReturn(bookPage);

        Page<BookSimpleResponseDto> result = bookService.getBooksByContributorId(pageable, 1L);

        assertEquals(1, result.getTotalElements());
        assertEquals(bookSimpleDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("도서 ID로 특정 도서 조회 - 성공")
    void testGetBookByIdSuccess() {
        when(bookRepository.findBookByBookId(anyLong())).thenReturn(Optional.of(bookResponseDto));

        BookResponseDto result = bookService.getBookById(1L);

        assertEquals(bookResponseDto, result);
    }

    @Test
    @DisplayName("도서 ID로 특정 도서 조회 - 실패")
    void testGetBookByIdFailure() {
        when(bookRepository.findBookByBookId(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    @DisplayName("도서 ID로 특정 도서 조회(수정용) - 성공")
    void getUpdateBookByBookId_shouldReturnBookWhenBookExists() {
        Long bookId = 1L;
        when(bookRepository.findUpdateBookByBookId(bookId)).thenReturn(Optional.of(bookUpdateResponseDto));

        BookUpdateResponseDto result = bookService.getUpdateBookByBookId(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.bookId());
        assertEquals("테스트 도서 제목", result.title());
        verify(bookRepository, times(1)).findUpdateBookByBookId(bookId);
    }

    @Test
    @DisplayName("도서 ID로 특정 도서 조회(수정용) - 실패")
    void getUpdateBookByBookId_shouldThrowExceptionWhenBookDoesNotExist() {
        Long bookId = 1L;
        when(bookRepository.findUpdateBookByBookId(bookId)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(
                BookNotFoundException.class,
                () -> bookService.getUpdateBookByBookId(bookId)
        );

        assertEquals("도서를 찾을 수 없습니다.", exception.getMessage());
        verify(bookRepository, times(1)).findUpdateBookByBookId(bookId);
    }

    @Test
    @DisplayName("도서 수정 성공")
    void testUpdateBookSuccess() {
        Long bookId = 1L;
        Book mockBook = createMockBook(1L, "Old Title", "경영출판사");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        when(publisherRepository.findByName("수정출판사")).thenReturn(Optional.of(createPublisher("수정출판사")));

        ContributorRole role = createContributorRole(1L, "지은이");
        Contributor contributor = createContributor(1L, role, "홍길동");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(role));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(createCategory(3L, null, "현대소설")));

        Tag updatedTag = new Tag(1L, "수정된 태그");
        when(tagRepository.findByName("수정된 태그")).thenReturn(Optional.of(updatedTag));

        Image thumbnailImage = new Image("new-thumbnail-url");
        Image detailImage = new Image("new-detail-url");
        when(imageRepository.save(any(Image.class))).thenReturn(thumbnailImage, detailImage);

        BookUpdateResultResponseDto responseDto = bookService.updateBook(bookId, bookUpdateRequestDto);

        assertNotNull(responseDto);
        assertEquals("Updated Title", responseDto.title());
        assertEquals("수정출판사", responseDto.publisherName());
        assertEquals("1234567890123", responseDto.isbn());

        verify(bookRepository, times(1)).findById(bookId);
        verify(publisherRepository, times(1)).findByName("수정출판사");
        verify(categoryRepository, times(1)).findById(3L);
        verify(tagRepository, times(1)).findByName("수정된 태그");
        verify(imageRepository, times(2)).save(any(Image.class));
    }

    @Test
    @DisplayName("썸네일 이미지 삭제 후 기본 이미지로 대체")
    void testUpdateBookWithRemoveThumbnailImage() {
        Long bookId = 1L;
        Book mockBook = createMockBook(1L, "Old Title", "경영출판사");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        when(publisherRepository.findByName("수정출판사")).thenReturn(Optional.of(createPublisher("수정출판사")));

        ContributorRole role = createContributorRole(1L, "지은이");
        Contributor contributor = createContributor(1L, role, "홍길동");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(role));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(createCategory(3L, null, "현대소설")));

        Tag updatedTag = new Tag(1L, "수정된 태그");
        when(tagRepository.findByName("수정된 태그")).thenReturn(Optional.of(updatedTag));

        String defaultImageUrl = "http://image.toast.com/aaaacko/ejoping/book/default/default-book-image.jpg";
        Image defaultImage = new Image(defaultImageUrl);
        when(imageRepository.save(any(Image.class))).thenReturn(defaultImage);

        when(bookImageRepository.findByBookAndImageType(any(Book.class), anyString()))
                .thenReturn(List.of(
                        new BookImage(mockBook, new Image("http://example.com/image1.jpg"), "썸네일")
                ));

        BookUpdateRequestDto requestDto = createMockUpdateRequestDto(
                true,
                false,
                null,
                null
        );

        BookUpdateResultResponseDto responseDto = bookService.updateBook(bookId, requestDto);

        assertEquals(defaultImageUrl, responseDto.thumbnailImageUrl());
        verify(bookImageRepository, times(1)).delete(any(BookImage.class));
        verify(imageRepository, times(1)).save(any(Image.class));
        verify(bookImageRepository, times(1)).save(any(BookImage.class));
    }

    @Test
    @DisplayName("썸네일 이미지가 새 URL로 대체")
    void testUpdateBookWithNewThumbnailUrl() {
        Long bookId = 1L;
        Book mockBook = createMockBook(1L, "Old Title", "경영출판사");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        when(publisherRepository.findByName("수정출판사")).thenReturn(Optional.of(createPublisher("수정출판사")));

        ContributorRole role = createContributorRole(1L, "지은이");
        Contributor contributor = createContributor(1L, role, "홍길동");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(role));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(createCategory(3L, null, "현대소설")));

        Tag updatedTag = new Tag(1L, "수정된 태그");
        when(tagRepository.findByName("수정된 태그")).thenReturn(Optional.of(updatedTag));

        String newThumbnailUrl = "http://new-thumbnail-url.com/image.jpg";
        Image newThumbnailImage = new Image(newThumbnailUrl);
        when(imageRepository.save(any(Image.class))).thenReturn(newThumbnailImage);

        Image existingThumbnailImage = new Image("http://example.com/old-thumbnail.jpg");
        BookImage existingBookThumbnail = new BookImage(mockBook, existingThumbnailImage, "썸네일");
        when(bookImageRepository.findByBookAndImageType(mockBook, "썸네일"))
                .thenReturn(List.of(existingBookThumbnail));

        BookUpdateRequestDto requestDto = createMockUpdateRequestDto(
                false,
                false,
                newThumbnailUrl,
                null
        );

        BookUpdateResultResponseDto responseDto = bookService.updateBook(bookId, requestDto);

        assertEquals(newThumbnailUrl, responseDto.thumbnailImageUrl());
        verify(bookImageRepository, times(1)).delete(existingBookThumbnail);
        verify(imageRepository, times(1)).save(any(Image.class));
        verify(bookImageRepository, times(1)).save(any(BookImage.class));
    }

    @Test
    @DisplayName("기존 썸네일 이미지를 유지")
    void testUpdateBookWithExistingThumbnail() {
        Long bookId = 1L;
        Book mockBook = createMockBook(1L, "Old Title", "경영출판사");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        when(publisherRepository.findByName("수정출판사")).thenReturn(Optional.of(createPublisher("수정출판사")));

        ContributorRole role = createContributorRole(1L, "지은이");
        Contributor contributor = createContributor(1L, role, "홍길동");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(role));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(createCategory(3L, null, "현대소설")));

        Tag updatedTag = new Tag(1L, "수정된 태그");
        when(tagRepository.findByName("수정된 태그")).thenReturn(Optional.of(updatedTag));

        String existingThumbnailUrl = "http://existing-thumbnail-url.com/image.jpg";
        Image existingThumbnailImage = new Image(existingThumbnailUrl);
        when(bookImageRepository.findByBookAndImageType(mockBook, "썸네일"))
                .thenReturn(List.of(new BookImage(mockBook, existingThumbnailImage, "썸네일")));

        BookUpdateRequestDto requestDto = createMockUpdateRequestDto(
                false,
                false,
                null,
                null
        );

        BookUpdateResultResponseDto responseDto = bookService.updateBook(bookId, requestDto);

        assertEquals(existingThumbnailUrl, responseDto.thumbnailImageUrl());
        verify(bookImageRepository, never()).delete(any(BookImage.class));
        verify(imageRepository, never()).save(any(Image.class));
        verify(bookImageRepository, never()).save(any(BookImage.class));
    }

    @Test
    @DisplayName("상세 이미지가 새 URL로 대체")
    void testUpdateBookWithNewDetailUrl() {
        Long bookId = 1L;
        Book mockBook = createMockBook(1L, "Old Title", "경영출판사");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        when(publisherRepository.findByName("수정출판사")).thenReturn(Optional.of(createPublisher("수정출판사")));

        ContributorRole role = createContributorRole(1L, "지은이");
        Contributor contributor = createContributor(1L, role, "홍길동");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(role));
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributor));

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(createCategory(3L, null, "현대소설")));

        Tag updatedTag = new Tag(1L, "수정된 태그");
        when(tagRepository.findByName("수정된 태그")).thenReturn(Optional.of(updatedTag));

        String newDetailUrl = "http://new-detail-url.com/image.jpg";
        Image newDetailImage = new Image(newDetailUrl);
        when(imageRepository.save(any(Image.class))).thenReturn(newDetailImage);

        when(bookImageRepository.findByBookAndImageType(any(Book.class), anyString()))
                .thenReturn(List.of(
                        new BookImage(mockBook, new Image("http://example.com/image1.jpg"), "썸네일")
                ));

        BookUpdateRequestDto requestDto = createMockUpdateRequestDto(
                false,
                false,
                null,
                newDetailUrl
        );

        BookUpdateResultResponseDto responseDto = bookService.updateBook(bookId, requestDto);

        assertEquals(newDetailUrl, responseDto.detailImageUrl());
        verify(bookImageRepository, times(1)).delete(any(BookImage.class));
        verify(imageRepository, times(1)).save(any(Image.class));
        verify(bookImageRepository, times(1)).save(any(BookImage.class));
    }

    @Test
    @DisplayName("도서 수정 실패 - 도서 없음")
    void testUpdateBookFail_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(1L, bookUpdateRequestDto));

        verify(bookRepository, times(1)).findById(1L);
        verifyNoInteractions(publisherRepository, categoryRepository, tagRepository, imageRepository);
    }

    @Test
    @DisplayName("도서 수정 실패 - 출판사 없음")
    void testUpdateBookFail_PublisherNotFound() {
        Long bookId = 1L;
        Book mockBook = createMockBook(1L, "Old Title", "경영출판사");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        when(publisherRepository.findByName("수정출판사")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> bookService.updateBook(bookId, bookUpdateRequestDto));

        verify(bookRepository, times(1)).findById(bookId);
        verify(publisherRepository, times(1)).findByName("수정출판사");
        verifyNoInteractions(categoryRepository, tagRepository, imageRepository);
    }

    @Test
    @DisplayName("도서 비활성화 - 성공")
    void testDeactivateBookSuccess() {
        Long bookId = 1L;
        Book mockBook = createMockBook(1L, "Old Title", "경영출판사");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        bookService.deactivateBook(1L);
        assertFalse(mockBook.isActive(), "도서가 비활성화되지 않았습니다.");
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("도서 비활성화 - 실패 (도서 없음)")
    void testDeactivateBookFailure() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.deactivateBook(1L));
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getContributorListForAPI - 성공적으로 기여자 리스트 반환")
    void testGetContributorListForAPISuccess() {
        String contributorText = "홍길동 (지은이), 김철수 (편집자)";

        ContributorRole roleAuthor = createContributorRole(1L, "지은이");
        ContributorRole roleEditor = createContributorRole(2L, "편집자");

        Contributor contributor1 = createContributor(1L, roleAuthor, "홍길동");
        Contributor contributor2 = createContributor(2L, roleEditor, "김철수");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(roleAuthor));
        when(contributorRoleRepository.findByName("편집자")).thenReturn(Optional.of(roleEditor));

        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.of(contributor1));
        when(contributorRepository.findByName("김철수")).thenReturn(Optional.of(contributor2));

        List<ContributorResponseDto> result = bookService.getContributorListForAPI(contributorText);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("홍길동", result.get(0).name());
        assertEquals("김철수", result.get(1).name());
        verify(contributorRoleRepository, times(1)).findByName("지은이");
        verify(contributorRoleRepository, times(1)).findByName("편집자");
        verify(contributorRepository, times(1)).findByName("홍길동");
        verify(contributorRepository, times(1)).findByName("김철수");
    }

    @Test
    @DisplayName("getContributorListForAPI - 역할이 없는 경우 새로 생성")
    void testGetContributorListForAPIRoleNotFound() {
        String contributorText = "홍길동 (신규역할)";

        ContributorRole newRole = createContributorRole(1L, "신규역할");
        Contributor newContributor = createContributor(1L, newRole, "홍길동");

        when(contributorRoleRepository.findByName("신규역할")).thenReturn(Optional.empty());
        when(contributorRoleRepository.save(any(ContributorRole.class))).thenReturn(newRole);
        when(contributorRepository.findByName("홍길동")).thenReturn(Optional.empty());
        when(contributorRepository.save(any(Contributor.class))).thenReturn(newContributor);

        List<ContributorResponseDto> result = bookService.getContributorListForAPI(contributorText);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("홍길동", result.getFirst().name());
        assertEquals("신규역할", newRole.getName());
        verify(contributorRoleRepository, times(1)).findByName("신규역할");
        verify(contributorRoleRepository, times(1)).save(any(ContributorRole.class));
        verify(contributorRepository, times(1)).save(any(Contributor.class));
    }

    @Test
    @DisplayName("getLowestLevelCategory - 성공적으로 최하위 카테고리 반환")
    void testGetLowestLevelCategorySuccess() {
        String categoryText = "도서 > 소설 > 현대소설";

        Category parentCategory1 = createCategory(1L, null, "도서");
        Category parentCategory2 = createCategory(2L, parentCategory1, "소설");
        Category lowestCategory = createCategory(3L, parentCategory2, "현대소설");

        when(categoryRepository.findByName("도서")).thenReturn(Optional.of(parentCategory1));
        when(categoryRepository.findByName("소설")).thenReturn(Optional.of(parentCategory2));
        when(categoryRepository.findByName("현대소설")).thenReturn(Optional.of(lowestCategory));

        Category result = bookService.getLowestLevelCategory(categoryText);

        assertNotNull(result);
        assertEquals("현대소설", result.getName());
        verify(categoryRepository, times(1)).findByName("도서");
        verify(categoryRepository, times(1)).findByName("소설");
        verify(categoryRepository, times(1)).findByName("현대소설");
    }

    @Test
    @DisplayName("getLowestLevelCategory - 카테고리가 없는 경우 새로 생성")
    void testGetLowestLevelCategoryNotFound() {
        String categoryText = "도서 > 소설 > 신규소설";

        Category parentCategory1 = createCategory(1L, null, "도서");
        Category parentCategory2 = createCategory(2L, parentCategory1, "소설");
        Category newCategory = createCategory(3L, parentCategory2, "신규소설");

        when(categoryRepository.findByName("도서")).thenReturn(Optional.of(parentCategory1));
        when(categoryRepository.findByName("소설")).thenReturn(Optional.of(parentCategory2));
        when(categoryRepository.findByName("신규소설")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        Category result = bookService.getLowestLevelCategory(categoryText);

        assertNotNull(result);
        assertEquals("신규소설", result.getName());
        verify(categoryRepository, times(1)).findByName("도서");
        verify(categoryRepository, times(1)).findByName("소설");
        verify(categoryRepository, times(1)).findByName("신규소설");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("getCategoryHierarchy - 하위 카테고리 ID가 존재하는 경우")
    void testGetCategoryHierarchyWithBottomCategory() {
        Long topCategoryId = 1L;
        Long middleCategoryId = 2L;
        Long bottomCategoryId = 3L;

        Category bottomCategory = createCategory(bottomCategoryId, null, "하위 카테고리");

        when(categoryRepository.findById(bottomCategoryId)).thenReturn(Optional.of(bottomCategory));

        Category result = bookService.getCategoryHierarchy(topCategoryId, middleCategoryId, bottomCategoryId);

        assertNotNull(result);
        assertEquals("하위 카테고리", result.getName());
        verify(categoryRepository, times(1)).findById(bottomCategoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("getCategoryHierarchy - 중간 카테고리 ID만 존재하는 경우")
    void testGetCategoryHierarchyWithMiddleCategory() {
        Long topCategoryId = 1L;
        Long middleCategoryId = 2L;
        Long bottomCategoryId = null;

        Category middleCategory = createCategory(middleCategoryId, null, "중간 카테고리");

        when(categoryRepository.findById(middleCategoryId)).thenReturn(Optional.of(middleCategory));

        Category result = bookService.getCategoryHierarchy(topCategoryId, middleCategoryId, bottomCategoryId);

        assertNotNull(result);
        assertEquals("중간 카테고리", result.getName());
        verify(categoryRepository, times(1)).findById(middleCategoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("getCategoryHierarchy - 상위 카테고리 ID만 존재하는 경우")
    void testGetCategoryHierarchyWithTopCategory() {
        Long topCategoryId = 1L;
        Long middleCategoryId = null;
        Long bottomCategoryId = null;

        Category topCategory = createCategory(topCategoryId, null, "상위 카테고리");

        when(categoryRepository.findById(topCategoryId)).thenReturn(Optional.of(topCategory));

        Category result = bookService.getCategoryHierarchy(topCategoryId, middleCategoryId, bottomCategoryId);

        assertNotNull(result);
        assertEquals("상위 카테고리", result.getName());
        verify(categoryRepository, times(1)).findById(topCategoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("getCategoryHierarchy - 카테고리 ID가 존재하지 않는 경우")
    void testGetCategoryHierarchyCategoryNotFound() {
        Long topCategoryId = null;
        Long middleCategoryId = null;
        Long bottomCategoryId = 3L;

        when(categoryRepository.findById(bottomCategoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> bookService.getCategoryHierarchy(topCategoryId, middleCategoryId, bottomCategoryId));

        verify(categoryRepository, times(1)).findById(bottomCategoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("getCategoryHierarchy - 카테고리 ID가 모두 null인 경우")
    void testGetCategoryHierarchyWithNoCategoryIds() {
        Long topCategoryId = null;
        Long middleCategoryId = null;
        Long bottomCategoryId = null;

        assertThrows(IllegalArgumentException.class,
                () -> bookService.getCategoryHierarchy(topCategoryId, middleCategoryId, bottomCategoryId));

        verifyNoInteractions(categoryRepository);
    }

    @Test
    @DisplayName("associateBookWithTag - 태그 리스트가 빈 문자열([])인 경우")
    void testAssociateBookWithTagEmptyList() {
        Book mockBook = createMockBook(1L, "Test Book", "Test Publisher");
        List<String> tagList = List.of("[]");

        List<TagResponseDto> result = bookService.associateBookWithTag(mockBook, tagList);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "빈 태그 리스트로 변환되지 않았습니다.");
        verifyNoInteractions(tagRepository, bookTagRepository);
    }

    @Test
    @DisplayName("associateBookWithTag - tagList가 완전히 비어 있는 경우")
    void testAssociateBookWithTagEmptyListDirect() {
        Book mockBook = createMockBook(1L, "Test Book", "Test Publisher");
        List<String> tagList = Collections.emptyList();

        List<TagResponseDto> result = bookService.associateBookWithTag(mockBook, tagList);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "태그 리스트가 비어 있을 때 반환 값이 빈 리스트가 아닙니다.");
        verifyNoInteractions(tagRepository, bookTagRepository);
    }

    @Test
    @DisplayName("getContributorList - contributorListJson이 null인 경우")
    void testGetContributorListNullJson() {
        String contributorListJson = null;

        List<ContributorResponseDto> result = bookService.getContributorList(contributorListJson);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "contributorListJson이 null일 때 빈 리스트가 반환되지 않았습니다.");
    }

    @Test
    @DisplayName("getContributorList - contributorListJson이 빈 문자열인 경우")
    void testGetContributorListEmptyJson() {
        String contributorListJson = "";

        List<ContributorResponseDto> result = bookService.getContributorList(contributorListJson);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "contributorListJson이 빈 문자열일 때 빈 리스트가 반환되지 않았습니다.");
    }

    @Test
    @DisplayName("getContributorList - name 또는 role이 누락된 경우")
    void testGetContributorListMissingNameOrRole() {
        String contributorListJson = "[{\"name\": \"홍길동\", \"role\": null}]";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookService.getContributorList(contributorListJson));

        assertEquals("Contributor name or role is missing", exception.getMessage());
    }

    @Test
    @DisplayName("getContributorList - JSON 변환 중 IOException 발생")
    void testGetContributorListJsonProcessingException() {
        String invalidJson = "invalid json";

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.getContributorList(invalidJson));

        assertEquals("Error processing contributor list JSON", exception.getMessage());
    }

    @Test
    void removeExistingImages_shouldRemoveImagesAndUnusedImageEntities() {
        Book mockBook = new Book();
        String imageType = "썸네일";

        Image mockImage1 = new Image("https://example.com/image1.jpg");
        Image mockImage2 = new Image("https://example.com/image2.jpg");

        BookImage bookImage1 = new BookImage(mockBook, mockImage1, imageType);
        BookImage bookImage2 = new BookImage(mockBook, mockImage2, imageType);

        List<BookImage> existingBookImages = List.of(bookImage1, bookImage2);

        when(bookImageRepository.findByBookAndImageType(mockBook, imageType)).thenReturn(existingBookImages);
        when(bookImageRepository.existsByImage(mockImage1)).thenReturn(false);
        when(bookImageRepository.existsByImage(mockImage2)).thenReturn(true);

        bookService.removeExistingImages(mockBook, imageType);

        verify(bookImageRepository, times(1)).findByBookAndImageType(mockBook, imageType);
        verify(bookImageRepository, times(1)).delete(bookImage1);
        verify(bookImageRepository, times(1)).delete(bookImage2);
        verify(bookImageRepository, times(1)).existsByImage(mockImage1);
        verify(bookImageRepository, times(1)).existsByImage(mockImage2);
        verify(imageRepository, times(1)).delete(mockImage1);
        verify(imageRepository, never()).delete(mockImage2);
    }

    @Test
    @DisplayName("도서 잔여 수량 조회 - 성공")
    void getBookRemainQuantity_shouldReturnRemainQuantityWhenBookExists() {
        Long bookId = 1L;
        int remainQuantity = 100;

        when(bookRepository.findRemainQuantityByBookId(bookId)).thenReturn(Optional.of(remainQuantity));

        int result = bookService.getBookRemainQuantity(bookId);

        assertEquals(remainQuantity, result);
        verify(bookRepository, times(1)).findRemainQuantityByBookId(bookId);
    }

    @Test
    @DisplayName("도서 잔여 수량 조회 - 실패")
    void getBookRemainQuantity_shouldThrowExceptionWhenBookNotFound() {
        Long bookId = 1L;

        when(bookRepository.findRemainQuantityByBookId(bookId)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.getBookRemainQuantity(bookId));
        assertEquals("도서를 찾을 수 없습니다.", exception.getMessage());
        verify(bookRepository, times(1)).findRemainQuantityByBookId(bookId);
    }
}
