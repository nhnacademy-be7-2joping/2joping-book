package com.nhnacademy.bookstore.bookset.book.service;

import com.nhnacademy.bookstore.bookset.book.dto.request.*;
import com.nhnacademy.bookstore.bookset.book.dto.response.*;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookCategoryRepository;
import com.nhnacademy.bookstore.bookset.book.repository.BookContributorRepository;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRepository;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.entity.BookTag;
import com.nhnacademy.bookstore.bookset.tag.entity.Tag;
import com.nhnacademy.bookstore.bookset.tag.repository.BookTagRepository;
import com.nhnacademy.bookstore.bookset.tag.repository.TagRepository;
import com.nhnacademy.bookstore.common.error.exception.bookset.book.BookNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.publisher.PublisherNotFoundException;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstore.imageset.entity.BookImage;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.repository.BookImageRepository;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

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
    private BookContributorRepository bookContributorRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookCategoryRepository bookCategoryRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private BookTagRepository bookTagRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private BookImageRepository bookImageRepository;

    private BookSimpleResponseDto bookSimpleDto;
    private BookResponseDto bookResponseDto;
    private Page<BookSimpleResponseDto> bookPage;
    private List<BookContributorResponseDto> contributors;

    @BeforeEach
    void setUp() {

        // BookContributorResponseDto 리스트 초기화
        contributors = List.of(
                new BookContributorResponseDto(1L, "Contributor 1", 1L, "Author")
        );

        // BookSimpleResponseDto 및 Page 초기화
        bookSimpleDto = new BookSimpleResponseDto(
                1L, "thumbnail1", "Book Title 1", 15000, "Publisher 1", 20000, true,
                contributors, List.of("Category 1", "Category 2")
        );

        bookPage = new PageImpl<>(List.of(bookSimpleDto));

        // BookResponseDto 초기화
        bookResponseDto = new BookResponseDto(
                1L, "Publisher 1", "Book Title 1", "Description", LocalDate.of(2023, 10, 29),
                "1234567890123", 20000, 15000, true, true, 10, 0, 0,
                contributors, List.of("Category 1", "Category 2"), List.of(new BookTagResponseDto(1L,"Tag 1")),"thumbnail1"
        );
    }

    private BookUpdateRequestDto createUpdateRequestDto(String publisherName, String tagText) {
        BookUpdateHtmlRequestDto htmlRequestDto = new BookUpdateHtmlRequestDto(
                "Updated Title", "Updated Description", publisherName, LocalDate.of(2023, 10, 29),
                "1234567890122", 20000, 15000, true, true, 10, "김지은 (지은이), 김엮은 (엮은이)",
                "국내도서>어린이>어린이 영어", tagText, false, false);

        ImageUrlRequestDto imageUrlRequestDto = new ImageUrlRequestDto("thumbnail.jpg", "detail.jpg");
        return new BookUpdateRequestDto(htmlRequestDto, imageUrlRequestDto);
        }

    @Test
    @DisplayName("기여자 목록 파싱 및 생성 테스트")
    void testGetContributorList() {
        String text = "김지은 (지은이), 김엮은 (엮은이)";

        ContributorRole roleAuthor = new ContributorRole(1L, "지은이");
        ContributorRole roleEditor = new ContributorRole(2L, "엮은이");

        Contributor contributorAuthor = new Contributor(1L, roleAuthor, "김지은", true);
        Contributor contributorEditor = new Contributor(2L, roleEditor, "김엮은", true);

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(roleAuthor));
        when(contributorRoleRepository.findByName("엮은이")).thenReturn(Optional.of(roleEditor));
        when(contributorRepository.findByName("김지은")).thenReturn(Optional.empty());
        when(contributorRepository.findByName("김엮은")).thenReturn(Optional.empty());
        when(contributorRepository.save(any(Contributor.class)))
                .thenAnswer(invocation -> {
                    Contributor argument = invocation.getArgument(0);
                    if (argument.getName().equals("김지은")) {
                        return contributorAuthor;
                    } else if (argument.getName().equals("김엮은")) {
                        return contributorEditor;
                    }
                    return argument;
                });

        List<ContributorResponseDto> result = bookService.getContributorList(text);

        assertEquals(2, result.size());
        assertEquals("김지은", result.get(0).name());
        assertEquals("김엮은", result.get(1).name());
        assertEquals(1L, result.get(0).contributorRoleId());
        assertEquals(2L, result.get(1).contributorRoleId());

        verify(contributorRepository, times(2)).save(any(Contributor.class));
    }

    @Test
    @DisplayName("카테고리 최하위 레벨 조회 테스트")
    void testGetLowestLevelCategory() {
        String categoryText = "국내도서 > 경제경영 > 트렌드/미래전망";

        Category domesticBooksCategory = new Category(1L, null, "국내도서", true);
        Category businessEconomyCategory = new Category(2L, domesticBooksCategory, "경제경영", true);
        Category trendFutureCategory = new Category(3L, businessEconomyCategory, "트렌드/미래전망", true);

        when(categoryRepository.findByName("국내도서")).thenReturn(Optional.of(domesticBooksCategory));
        when(categoryRepository.findByName("경제경영")).thenReturn(Optional.of(businessEconomyCategory));
        when(categoryRepository.findByName("트렌드/미래전망")).thenReturn(Optional.of(trendFutureCategory));

        Category result = bookService.getLowestLevelCategory(categoryText);

        assertEquals("트렌드/미래전망", result.getName());
        assertEquals(3L, result.getCategoryId());
        assertNotNull(result.getParentCategory());
        assertEquals("경제경영", result.getParentCategory().getName());
        assertEquals("국내도서", result.getParentCategory().getParentCategory().getName());
    }


    @Test
    @DisplayName("도서와 태그 연관 테스트")
    void testAssociateBookWithTag() {
        Publisher publisher = new Publisher(1L, "Test Publisher");
        Book book = new Book(
                null,
                publisher,
                "Test Title",
                "Test Description",
                LocalDate.now(),
                "1234567890123",
                20000,
                18000,
                true,
                true,
                10,
                0,
                0
        );

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            Field bookIdField = Book.class.getDeclaredField("bookId");
            bookIdField.setAccessible(true);
            bookIdField.set(savedBook, 1L);
            return savedBook;
        });

        String tagText = "재밌는, 따뜻한";
        Tag funTag = new Tag(1L, "재밌는");
        Tag warmTag = new Tag(2L, "따뜻한");

        when(tagRepository.findByName("재밌는")).thenReturn(Optional.of(funTag));
        when(tagRepository.findByName("따뜻한")).thenReturn(Optional.of(warmTag));
        when(bookTagRepository.save(any(BookTag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book savedBook = bookRepository.save(book);
        List<TagResponseDto> result = bookService.associateBookWithTag(savedBook, tagText);

        assertEquals(2, result.size());
        assertEquals("재밌는", result.get(0).name());
        assertEquals("따뜻한", result.get(1).name());
        assertNotNull(savedBook.getBookId());
        assertEquals(1L, savedBook.getBookId());
    }

    @Test
    @DisplayName("도서 생성 (기여자와 카테고리, 태그 포함)")
    void testCreateBook() {
        BookCreateHtmlRequestDto bookHtmlRequest = mock(BookCreateHtmlRequestDto.class);
        ImageUrlRequestDto imageUrlRequest = mock(ImageUrlRequestDto.class);

        when(bookHtmlRequest.publisherName()).thenReturn("Penguin");
        when(publisherRepository.findByName("Penguin")).thenReturn(Optional.of(new Publisher(1L, "Penguin")));

        when(bookHtmlRequest.title()).thenReturn("Sample Book");
        when(bookHtmlRequest.description()).thenReturn("A test book");
        when(bookHtmlRequest.isbn()).thenReturn("1234567890123");

        String contributorText = "김지은 (지은이), 김엮은 (엮은이)";
        when(bookHtmlRequest.contributorList()).thenReturn(contributorText);

        ContributorRole roleAuthor = new ContributorRole(1L, "지은이");
        ContributorRole roleEditor = new ContributorRole(2L, "엮은이");

        Contributor contributorAuthor = new Contributor(1L, roleAuthor, "김지은", true);
        Contributor contributorEditor = new Contributor(2L, roleEditor, "김엮은", true);

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(roleAuthor));
        when(contributorRoleRepository.findByName("엮은이")).thenReturn(Optional.of(roleEditor));
        when(contributorRepository.findByName("김지은")).thenReturn(Optional.of(contributorAuthor));
        when(contributorRepository.findByName("김엮은")).thenReturn(Optional.of(contributorEditor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributorAuthor));
        when(contributorRepository.findById(2L)).thenReturn(Optional.of(contributorEditor));

        String categoryText = "국내도서 > 소설 > 현대소설";
        when(bookHtmlRequest.category()).thenReturn(categoryText);

        Category category1 = new Category(1L, null, "국내도서", true);
        Category category2 = new Category(2L, category1, "소설", true);
        Category category3 = new Category(3L, category2, "현대소설", true);

        when(categoryRepository.findByName("국내도서")).thenReturn(Optional.of(category1));
        when(categoryRepository.findByName("소설")).thenReturn(Optional.of(category2));
        when(categoryRepository.findByName("현대소설")).thenReturn(Optional.of(category3));

        String tagText = "재밌는, 감동적인, 교육적인";
        when(bookHtmlRequest.tagList()).thenReturn(tagText);

        Tag tag1 = new Tag(1L, "재밌는");
        Tag tag2 = new Tag(2L, "감동적인");
        Tag tag3 = new Tag(3L, "교육적인");

        when(tagRepository.findByName("재밌는")).thenReturn(Optional.of(tag1));
        when(tagRepository.findByName("감동적인")).thenReturn(Optional.of(tag2));
        when(tagRepository.findByName("교육적인")).thenReturn(Optional.of(tag3));

        when(bookTagRepository.save(any(BookTag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookCreateRequestDto requestDto = new BookCreateRequestDto(bookHtmlRequest, imageUrlRequest);
        BookCreateResponseDto result = bookService.createBook(requestDto);

        assertNotNull(result);
        assertEquals("Sample Book", result.title());
        assertEquals("1234567890123", result.isbn());

        verify(contributorRepository).findById(1L);
        verify(contributorRepository).findById(2L);
        verify(tagRepository).findByName("재밌는");
        verify(tagRepository).findByName("감동적인");
        verify(tagRepository).findByName("교육적인");
        verify(bookRepository).save(any(Book.class));
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
    @DisplayName("도서 수정 - 성공")
    void updateBook_Success() {
        Long bookId = 1L;

        Book mockBook = mock(Book.class);
        Publisher mockPublisher = new Publisher(1L, "Updated Publisher");
        Category domesticBooksCategory = new Category(1L, null, "국내도서");
        Category childrenCategory = new Category(2L, domesticBooksCategory, "어린이");
        Category childrenEnglishCategory = new Category(3L, childrenCategory, "어린이 영어");

        ContributorRole authorRole = new ContributorRole(1L, "지은이");
        ContributorRole editorRole = new ContributorRole(2L, "엮은이");

        Contributor contributorAuthor = new Contributor(1L, authorRole, "김지은", true);
        Contributor contributorEditor = new Contributor(2L, editorRole, "김엮은", true);

        Tag warmTag = new Tag(1L, "따뜻한");
        Tag funnyTag = new Tag(2L, "웃긴");

        BookUpdateRequestDto requestDto = createUpdateRequestDto("Updated Publisher", "따뜻한, 웃긴");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(mockBook.getPublisher()).thenReturn(mockPublisher);
        when(mockBook.getTitle()).thenReturn("Updated Title");
        when(mockBook.getDescription()).thenReturn("Updated Description");
        when(mockBook.getPublishedDate()).thenReturn(LocalDate.of(2023, 10, 29));
        when(mockBook.getIsbn()).thenReturn("1234567890122");
        when(mockBook.getRetailPrice()).thenReturn(20000);
        when(mockBook.getSellingPrice()).thenReturn(15000);
        when(mockBook.isGiftWrappable()).thenReturn(true);
        when(mockBook.isActive()).thenReturn(true);
        when(mockBook.getRemainQuantity()).thenReturn(10);

        when(publisherRepository.findByName("Updated Publisher")).thenReturn(Optional.of(mockPublisher));
        when(categoryRepository.findByName("국내도서")).thenReturn(Optional.of(domesticBooksCategory));
        when(categoryRepository.findByName("어린이")).thenReturn(Optional.of(childrenCategory));
        when(categoryRepository.findByName("어린이 영어")).thenReturn(Optional.of(childrenEnglishCategory));
        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.of(authorRole));
        when(contributorRoleRepository.findByName("엮은이")).thenReturn(Optional.of(editorRole));
        when(contributorRepository.findByName("김지은")).thenReturn(Optional.of(contributorAuthor));
        when(contributorRepository.findByName("김엮은")).thenReturn(Optional.of(contributorEditor));
        when(contributorRepository.findById(1L)).thenReturn(Optional.of(contributorAuthor));
        when(contributorRepository.findById(2L)).thenReturn(Optional.of(contributorEditor));
        when(tagRepository.findByName("따뜻한")).thenReturn(Optional.of(warmTag));
        when(tagRepository.findByName("웃긴")).thenReturn(Optional.of(funnyTag));

        Image mockThumbnailImage = new Image("thumbnail.jpg");
        Image mockDetailImage = new Image("detail.jpg");

        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> {
            Image image = invocation.getArgument(0);
            if (image.getUrl().equals("thumbnail.jpg")) {
                return mockThumbnailImage;
            } else if (image.getUrl().equals("detail.jpg")) {
                return mockDetailImage;
            }
            return null;
        });

        when(bookImageRepository.save(any(BookImage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookUpdateResultResponseDto result = bookService.updateBook(bookId, requestDto);

        assertNotNull(result);
        assertEquals("Updated Title", result.title());
        assertEquals("Updated Description", result.description());
        assertEquals("Updated Publisher", result.publisherName());
        assertEquals(LocalDate.of(2023, 10, 29), result.publishedDate());
        assertEquals("1234567890122", result.isbn());
        assertEquals(20000, result.retailPrice());
        assertEquals(15000, result.sellingPrice());
        assertTrue(result.giftWrappable());
        assertTrue(result.isActive());
        assertEquals(10, result.remainQuantity());

        assertNotNull(result.contributorList());
        assertEquals(2, result.contributorList().size());
        assertEquals("김지은", result.contributorList().get(0).name());
        assertEquals("김엮은", result.contributorList().get(1).name());

        assertNotNull(result.category());
        assertEquals("어린이 영어", result.category().name());

        assertNotNull(result.tagList());
        assertEquals(2, result.tagList().size());
        assertEquals("따뜻한", result.tagList().get(0).name());
        assertEquals("웃긴", result.tagList().get(1).name());

        verify(imageRepository, times(2)).save(any(Image.class));
        verify(bookImageRepository, times(2)).save(any(BookImage.class));
    }

    @Test
    @DisplayName("도서 수정 - 실패 (도서 없음)")
    void updateBook_Fail_BookNotFound() {
        Long bookId = 999L;

        BookUpdateRequestDto requestDto = createUpdateRequestDto("Updated Publisher", "따뜻한, 웃긴");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, requestDto));

        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookCategoryRepository, bookImageRepository, imageRepository);
    }

    @Test
    @DisplayName("도서 수정 - 실패 (출판사 없음)")
    void updateBook_Fail_PublisherNotFound() {
        Long bookId = 1L;
        Book mockBook = mock(Book.class);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        BookUpdateHtmlRequestDto htmlRequestDto = new BookUpdateHtmlRequestDto(
                "Updated Title", "Updated Description", "Non-Existent Publisher", LocalDate.of(2023, 10, 29),
                "1234567890122", 20000, 15000, true, true, 10, "김지은 (지은이), 김엮은 (엮은이)",
                "국내도서>어린이>어린이 영어", "따뜻한, 웃긴", false, false);

        ImageUrlRequestDto imageUrlRequestDto = new ImageUrlRequestDto("thumbnail.jpg", "detail.jpg");

        BookUpdateRequestDto requestDto = new BookUpdateRequestDto(htmlRequestDto, imageUrlRequestDto);

        when(publisherRepository.findByName("Non-Existent Publisher")).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> bookService.updateBook(bookId, requestDto));

        verify(bookRepository, times(1)).findById(bookId);
        verify(publisherRepository, times(1)).findByName("Non-Existent Publisher");
    }

    @Test
    @DisplayName("도서 수정 - 실패 (기여자 역할 없음)")
    void updateBook_Fail_ContributorRoleNotFound() {
        Long bookId = 1L;
        Book mockBook = mock(Book.class);
        Publisher mockPublisher = new Publisher(1L, "Updated Publisher");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(publisherRepository.findByName("Updated Publisher")).thenReturn(Optional.of(mockPublisher));

        Category domesticBooksCategory = new Category(1L, null, "국내도서");
        Category childrenCategory = new Category(2L, domesticBooksCategory, "어린이");
        Category childrenEnglishCategory = new Category(3L, childrenCategory, "어린이 영어");

        when(categoryRepository.findByName("국내도서")).thenReturn(Optional.of(domesticBooksCategory));
        when(categoryRepository.findByName("어린이")).thenReturn(Optional.of(childrenCategory));
        when(categoryRepository.findByName("어린이 영어")).thenReturn(Optional.of(childrenEnglishCategory));

        BookUpdateRequestDto requestDto = createUpdateRequestDto("Updated Publisher", "따뜻한, 웃긴");

        when(contributorRoleRepository.findByName("지은이")).thenReturn(Optional.empty()); // 역할 찾지 못하도록 설정

        assertThrows(ContributorRoleNotFoundException.class, () -> bookService.updateBook(bookId, requestDto));

        verify(bookRepository, times(1)).findById(bookId);
        verify(contributorRoleRepository, times(1)).findByName("지은이");
    }
}
