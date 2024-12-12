package com.nhnacademy.bookstore.bookset.book.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nhnacademy.bookstore.bookset.book.dto.request.*;
import com.nhnacademy.bookstore.bookset.book.dto.response.*;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.BookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.BookContributor;
import com.nhnacademy.bookstore.bookset.book.repository.BookCategoryRepository;
import com.nhnacademy.bookstore.bookset.book.repository.BookContributorRepository;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.contributor.dto.request.ContributorRoleRequestDto;
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
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CategoryNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.publisher.PublisherNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.tag.TagNotFoundException;
import com.nhnacademy.bookstore.imageset.entity.BookImage;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.repository.BookImageRepository;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final ContributorRepository contributorRepository;
    private final ContributorRoleRepository contributorRoleRepository;
    private final BookContributorRepository bookContributorRepository;
    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final ImageRepository imageRepository;
    private final BookImageRepository bookImageRepository;
    private final BookTagRepository bookTagRepository;
    private final ObjectMapper objectMapper;
    private final TagRepository tagRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 텍스트를 파싱하여 기여자를 생성하고 반환하는 메서드
     *
     * @param text 파싱할 기여자 텍스트 (이름과 역할을 포함)
     * @return 기여자 리스트 객체 (ContributorResponseDto)
     */
    @Override
    public List<ContributorResponseDto> getContributorListForAPI(String text) {
        String pattern = "([\\p{L}\\w\\s,]+) \\(([^)]+)\\)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(text);

        List<ContributorResponseDto> contributorDtos = new ArrayList<>();

        while (matcher.find()) {
            String[] rawNames = matcher.group(1).trim().split(",\\s*");
            List<String> names = new ArrayList<>();

            for (String rawName : rawNames) {
                if (!rawName.isBlank()) {
                    names.add(rawName.trim());
                }
            }

            String roleName = matcher.group(2).trim();

            ContributorRole role = contributorRoleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        ContributorRole newRole = new ContributorRole();
                        ContributorRoleRequestDto requestDto = new ContributorRoleRequestDto(roleName);
                        newRole.toEntity(requestDto);
                        return contributorRoleRepository.save(newRole);
                    });


            for (String name : names) {
                Contributor contributor = contributorRepository.findByName(name)
                        .orElseGet(() -> {
                            Contributor newContributor = new Contributor(null, role, name, true);
                            return contributorRepository.save(newContributor);
                        });

                contributorDtos.add(new ContributorResponseDto(
                        contributor.getContributorId(),
                        contributor.getContributorRole().getContributorRoleId(),
                        contributor.getName()
                ));
            }
        }
        return contributorDtos;
    }

    /**
     * 텍스트를 파싱하여 최하위 레벨의 카테고리를 반환하는 메서드
     *
     * @param categoryText 파싱할 카테고리 텍스트 (구분자 > 사용)
     * @return 최하위 레벨의 카테고리 객체 (Category)
     */
    public Category getLowestLevelCategory(String categoryText) {
        String[] categories = categoryText.split(">");
        int upperLimit = Math.min(3, categories.length);

        Category parentCategory = null;
        for (int i = 0; i < upperLimit; i++) {
            String categoryName = categories[i].trim();

            Category finalParentCategory = parentCategory;
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.updateName(categoryName);
                        newCategory.activate();
                        newCategory.updateParentCategory(finalParentCategory); // 상위 카테고리를 설정
                        return categoryRepository.save(newCategory);
                    });

            // 현재 카테고리를 부모 카테고리로 설정
            parentCategory = category;
        }
        return parentCategory;
    }

    /**
     * 도서와 태그를 연관짓는 메서드
     *
     * @param book    태그를 연관시킬 도서 객체
     * @param tagList 태그 목록 텍스트
     * @return 태그 리스트 객체 (TagResponseDto)
     */
    public List<TagResponseDto> associateBookWithTag(Book book, List<String> tagList) {
        // "[]" 문자열 처리
        if (tagList.size() == 1 && tagList.get(0).equals("[]")) {
            tagList = new ArrayList<>(); // 빈 리스트로 대체
        }

        List<TagResponseDto> tagResponseDtos = new ArrayList<>();
        for (String inputTag : tagList) {
            String tagName = inputTag.trim();
            Tag tag = tagRepository.findByName(tagName)
                    .orElseThrow(TagNotFoundException::new);

            BookTag bookTag = new BookTag(
                    new BookTag.BookTagId(book.getBookId(), tag.getTagId()),
                    book,
                    tag
            );
            bookTagRepository.save(bookTag);

            tagResponseDtos.add(new TagResponseDto(tag.getTagId(), tag.getName()));
        }

        return tagResponseDtos;
    }

    /**
     * 주어진 카테고리 ID를 기반으로 계층 구조에서 가장 하위 레벨의 카테고리를 반환하는 메서드
     *
     * @param topCategoryId
     * @param middleCategoryId
     * @param bottomCategoryId
     * @return 계층 구조에서 가장 하위 레벨의 카테고리 객체
     */
    public Category getCategoryHierarchy(Long topCategoryId, Long middleCategoryId, Long bottomCategoryId) {
        if (bottomCategoryId != null) {
            return categoryRepository.findById(bottomCategoryId)
                    .orElseThrow(CategoryNotFoundException::new);
        }
        if (middleCategoryId != null) {
            return categoryRepository.findById(middleCategoryId)
                    .orElseThrow(CategoryNotFoundException::new);
        }
        if (topCategoryId != null) {
            return categoryRepository.findById(topCategoryId)
                    .orElseThrow(CategoryNotFoundException::new);
        }
        throw new IllegalArgumentException("At least one category must be selected.");
    }

    /**
     * 기여자 리스트를 가져오는 메서드
     *
     * @param contributorListJson Json 형식의 기여자 리스트
     * @return 기여자 리스트 객체 (ContributorResponseDto)
     */
    @Override
    public List<ContributorResponseDto> getContributorList(String contributorListJson) {
        List<ContributorResponseDto> contributorDtos = new ArrayList<>();

        if (contributorListJson == null || contributorListJson.isEmpty()) {
            return contributorDtos;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> contributorList = objectMapper.readValue(contributorListJson, new TypeReference<>() {
            });

            for (Map<String, String> contributorMap : contributorList) {
                String name = contributorMap.get("name");
                String roleName = contributorMap.get("role");

                if (name == null || roleName == null) {
                    throw new IllegalArgumentException("Contributor name or role is missing");
                }

                ContributorRole role = contributorRoleRepository.findByName(roleName)
                        .orElseThrow(ContributorRoleNotFoundException::new);

                Contributor contributor = contributorRepository.findByName(name)
                        .orElseThrow(ContributorNotFoundException::new);

                contributorDtos.add(new ContributorResponseDto(
                        contributor.getContributorId(),
                        contributor.getContributorRole().getContributorRoleId(),
                        contributor.getName()
                ));
            }
        } catch (IOException ex) {
            // JSON 변환 중 발생한 예외 처리
            ex.printStackTrace();
            throw new RuntimeException("Error processing contributor list JSON");
        }

        return contributorDtos;
    }

    /**
     * 도서를 단독으로 등록하는 메서드
     *
     * @param bookCreateRequestDto 도서 등록 요청 정보 (BookCreateRequestDto)
     * @return 등록된 도서에 대한 응답 정보 (BookCreateResponseDto)
     */
    @Override
    public BookCreateResponseDto createBook(BookCreateRequestDto bookCreateRequestDto) {

        BookCreateHtmlRequestDto bookCreateHtmlRequestDto = bookCreateRequestDto.bookCreateHtmlRequestDto();
        ImageUrlRequestDto imageUrlRequestDto = bookCreateRequestDto.imageUrlRequestDto();

        Publisher publisher = publisherRepository.findByName(bookCreateHtmlRequestDto.publisherName())
                .orElseThrow(PublisherNotFoundException::new);

        Book book = new Book(
                null,
                publisher,
                bookCreateHtmlRequestDto.title(),
                bookCreateHtmlRequestDto.description(),
                bookCreateHtmlRequestDto.publishedDate(),
                bookCreateHtmlRequestDto.isbn(),
                bookCreateHtmlRequestDto.retailPrice(),
                bookCreateHtmlRequestDto.sellingPrice(),
                bookCreateHtmlRequestDto.giftWrappable(),
                bookCreateHtmlRequestDto.isActive(),
                bookCreateHtmlRequestDto.remainQuantity(),
                0,
                0, null
        );

        List<ContributorResponseDto> contributorResponseDtos = getContributorList(bookCreateHtmlRequestDto.contributorList());

        contributorResponseDtos.forEach(dto -> {
            Contributor contributor = contributorRepository.findById(dto.contributorId())
                    .orElseThrow(ContributorNotFoundException::new);

            bookContributorRepository.save(new BookContributor(
                    new BookContributor.BookContributorId(book.getBookId(), contributor.getContributorId()),
                    book,
                    contributor
            ));
        });

        Category category = getCategoryHierarchy(
                bookCreateRequestDto.bookCreateHtmlRequestDto().topCategoryId(),
                bookCreateRequestDto.bookCreateHtmlRequestDto().middleCategoryId(),
                bookCreateRequestDto.bookCreateHtmlRequestDto().bottomCategoryId()
        );

        bookCategoryRepository.save(new BookCategory(
                new BookCategory.BookCategoryId(book.getBookId(), category.getCategoryId()),
                book,
                category
        ));

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                category.getCategoryId(),
                category.getName(),
                category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null
        );

        List<TagResponseDto> tagResponseDtos;
        tagResponseDtos = associateBookWithTag(book, bookCreateHtmlRequestDto.tagList());

        String thumbnailImageUrl = imageUrlRequestDto.thumbnailImageUrl() != null ? imageUrlRequestDto.thumbnailImageUrl() : "http://image.toast.com/aaaacko/ejoping/book/default/default-book-image.jpg";
        String detailImageUrl = imageUrlRequestDto.detailImageUrl() != null ? imageUrlRequestDto.detailImageUrl() : "http://image.toast.com/aaaacko/ejoping/book/default/default-book-image.jpg";

        if (imageUrlRequestDto.thumbnailImageUrl() != null) {
            Image thumbnailImage = imageRepository.save(new Image(thumbnailImageUrl));
            bookImageRepository.save(new BookImage(book, thumbnailImage, "썸네일"));
        }

        if (imageUrlRequestDto.detailImageUrl() != null) {
            Image detailImage = imageRepository.save(new Image(detailImageUrl));
            bookImageRepository.save(new BookImage(book, detailImage, "상세"));
        }

        bookRepository.save(book);

        return new BookCreateResponseDto(
                book.getBookId(),
                book.getTitle(),
                book.getDescription(),
                book.getPublisher().getName(),
                book.getPublishedDate(),
                book.getIsbn(),
                book.getRetailPrice(),
                book.getSellingPrice(),
                book.isGiftWrappable(),
                book.isActive(),
                book.getRemainQuantity(),
                contributorResponseDtos,
                categoryResponseDto,
                tagResponseDtos,
                imageUrlRequestDto.thumbnailImageUrl(),
                imageUrlRequestDto.detailImageUrl()
        );
    }

    /**
     * 알라딘 API를 통해 도서를 등록하는 메서드
     *
     * @param query 검색명
     * @return 등록된 도서 리스트 객체 (BookCreateAPIResponseDto)
     * @throws ContributorNotFoundException 기여자를 찾을 수 없는 경우 발생
     */
    @Override
    public List<BookCreateAPIResponseDto> createBooks(String query) {

        String apiKey = "ttbdlugus1759001";
        String url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=" + apiKey + "&Query=" + query + "&QueryType=Title&MaxResults=5&start=1&SearchTarget=Book&output=JS&Version=20131101";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String jsonResponse = response.getBody();
        List<BookCreateAPIResponseDto> bookCreateResponseDtos = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            ArrayNode itemsNode = (ArrayNode) rootNode.path("item");

            for (JsonNode itemNode : itemsNode) {
                String publisherName = itemNode.path("publisher").asText();
                Publisher publisher = publisherRepository.findByName(publisherName)
                        .orElseGet(() -> publisherRepository.save(new Publisher(publisherName)));

                String title = itemNode.path("title").asText();
                String description = itemNode.path("description").asText();
                LocalDate publishedDate = LocalDate.parse(itemNode.path("pubDate").asText());
                String isbn = itemNode.path("isbn13").asText();
                int retailPrice = itemNode.path("priceStandard").asInt();
                int sellingPrice = itemNode.path("priceSales").asInt();
                String contributors = itemNode.path("author").asText();
                String categories = itemNode.path("categoryName").asText();
                String thumbnail = itemNode.path("cover").asText();

                Book book = new Book(
                        null,
                        publisher,
                        title,
                        description,
                        publishedDate,
                        isbn,
                        retailPrice,
                        sellingPrice,
                        true,
                        true,
                        1000,
                        0,
                        0, null
                );

                bookRepository.save(book);
                bookRepository.flush();

                List<ContributorResponseDto> contributorResponseDtos = getContributorListForAPI(contributors);

                contributorResponseDtos.forEach(dto -> {
                    Contributor contributor = contributorRepository.findById(dto.contributorId())
                            .orElseThrow(ContributorNotFoundException::new);

                    bookContributorRepository.save(new BookContributor(
                            new BookContributor.BookContributorId(book.getBookId(), contributor.getContributorId()),
                            book,
                            contributor
                    ));
                });

                Category category = getLowestLevelCategory(categories);

                bookCategoryRepository.save(new BookCategory(
                        new BookCategory.BookCategoryId(book.getBookId(), category.getCategoryId()),
                        book,
                        category
                ));

                CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                        category.getCategoryId(),
                        category.getName(),
                        category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null
                );

                Image image = imageRepository.save(new Image(thumbnail));
                bookImageRepository.save(new BookImage(book, image, "썸네일"));

                BookCreateAPIResponseDto bookCreateResponseDto = new BookCreateAPIResponseDto(
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
                        contributorResponseDtos,
                        categoryResponseDto,
                        thumbnail
                );
                bookCreateResponseDtos.add(bookCreateResponseDto);
            }
        } catch (Exception e) {
            throw new RuntimeException("데이터 처리 중 예외 발생", e);
        }
        return bookCreateResponseDtos;
    }

    /**
     * 전체 도서를 조회하는 메서드
     *
     * @return 도서 객체
     */
    @Transactional(readOnly = true)
    @Override
    public Page<BookSimpleResponseDto> getAllBooks(Pageable pageable) {
        Page<BookSimpleResponseDto> books = bookRepository.findAllBooks(pageable);
        return books;
    }

    /**
     * 카테고리로 도서를 조회하는 메서드
     *
     * @param categoryId
     * @return 도서 객체
     */

    @Transactional(readOnly = true)
    @Override
    public Page<BookSimpleResponseDto> getBooksByCategoryId(Pageable pageable, Long categoryId) {
        return bookRepository.findBooksByCategoryId(pageable, categoryId);
    }

    /**
     * 기여자로 도서를 조회하는 메서드
     *
     * @param contributorId
     * @return 도서 객체
     */

    @Transactional(readOnly = true)
    @Override
    public Page<BookSimpleResponseDto> getBooksByContributorId(Pageable pageable, Long contributorId) {
        return bookRepository.findBooksByContributorId(pageable, contributorId);
    }

    /**
     * 특정 도서를 조회하는 메서드
     *
     * @param bookId
     * @return 도서 객체
     */

    @Transactional(readOnly = true)
    @Override
    public BookResponseDto getBookById(Long bookId) {
        BookResponseDto book = bookRepository.findBookByBookId(bookId).orElseThrow(() -> new BookNotFoundException("도서를 찾을 수 없습니다."));
        return book;
    }

//    /**
//     * Id 리스트를 받아 도서리스트를 조회하는 메서드
//     * @param bookIds 특정 도서 아이디 리스트
//     * @return 도서목록
//     */
//    @Override
//    public List<BookResponseDto> getBooksById(List<Long> bookIds) {
//        // TODO 장바구니에서 도서 정보 조회 필요.
//        return List.of();
//    }

    /**
     * 특정 도서를 업데이트용으로 조회하는 메서드
     *
     * @param bookId
     * @return 도서 객체
     */
    @Override
    public BookUpdateResponseDto getUpdateBookByBookId(Long bookId) {
        BookUpdateResponseDto book = bookRepository.findUpdateBookByBookId(bookId).orElseThrow(() -> new BookNotFoundException("도서를 찾을 수 없습니다."));
        return book;
    }

    /**
     * 특정 도서를 업데이트하는 메서드
     *
     * @param bookId
     * @param bookUpdateRequestDto 도서 업데이트 요청 데이터가 담긴 DTO
     * @return 업데이트된 도서의 결과 정보가 담긴 DTO
     */
    @Transactional
    @Override
    public BookUpdateResultResponseDto updateBook(Long bookId, BookUpdateRequestDto bookUpdateRequestDto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("책을 찾을 수 없습니다."));

        BookUpdateHtmlRequestDto bookUpdateHtmlRequestDto = bookUpdateRequestDto.bookUpdateHtmlRequestDto();
        ImageUrlRequestDto imageUrlRequestDto = bookUpdateRequestDto.imageUrlRequestDto();

        Publisher publisher = publisherRepository.findByName(bookUpdateHtmlRequestDto.publisherName())
                .orElseThrow(PublisherNotFoundException::new);

        book.updateBook(
                bookUpdateHtmlRequestDto.title(),
                bookUpdateHtmlRequestDto.description(),
                publisher,
                bookUpdateHtmlRequestDto.publishedDate(),
                bookUpdateHtmlRequestDto.isbn(),
                bookUpdateHtmlRequestDto.retailPrice(),
                bookUpdateHtmlRequestDto.sellingPrice(),
                bookUpdateHtmlRequestDto.giftWrappable(),
                bookUpdateHtmlRequestDto.isActive(),
                bookUpdateHtmlRequestDto.remainQuantity()
        );

        bookContributorRepository.deleteByBook(book);

        List<ContributorResponseDto> contributorResponseDtos = getContributorList(bookUpdateHtmlRequestDto.contributorList());

        contributorResponseDtos.forEach(dto -> {
            Contributor contributor = contributorRepository.findById(dto.contributorId())
                    .orElseThrow(ContributorNotFoundException::new);

            bookContributorRepository.save(new BookContributor(
                    new BookContributor.BookContributorId(book.getBookId(), contributor.getContributorId()),
                    book,
                    contributor
            ));
        });

        bookCategoryRepository.deleteByBook(book);

        Category category = getCategoryHierarchy(
                bookUpdateRequestDto.bookUpdateHtmlRequestDto().topCategoryId(),
                bookUpdateRequestDto.bookUpdateHtmlRequestDto().middleCategoryId(),
                bookUpdateRequestDto.bookUpdateHtmlRequestDto().bottomCategoryId()
        );

        bookCategoryRepository.save(new BookCategory(
                new BookCategory.BookCategoryId(book.getBookId(), category.getCategoryId()),
                book,
                category
        ));

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                category.getCategoryId(),
                category.getName(),
                category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null
        );

        bookTagRepository.deleteByBook(book);
        List<TagResponseDto> tagResponseDtos = associateBookWithTag(book, bookUpdateHtmlRequestDto.tagList());

        String thumbnailImageUrl = null;
        String detailImageUrl = null;
        String defaultImageUrl = "http://image.toast.com/aaaacko/ejoping/book/default/default-book-image.jpg";

        if (bookUpdateHtmlRequestDto.removeThumbnailImage()) {
            removeExistingImages(book, "썸네일");
            Image thumbnailImage = imageRepository.save(new Image(defaultImageUrl));
            bookImageRepository.save(new BookImage(book, thumbnailImage, "썸네일"));
            thumbnailImageUrl = thumbnailImage.getUrl();
        } else if (imageUrlRequestDto.thumbnailImageUrl() != null && !imageUrlRequestDto.thumbnailImageUrl().isBlank()) {
            thumbnailImageUrl = imageUrlRequestDto.thumbnailImageUrl();
            removeExistingImages(book, "썸네일");
            Image thumbnailImage = imageRepository.save(new Image(thumbnailImageUrl));
            bookImageRepository.save(new BookImage(book, thumbnailImage, "썸네일"));
        } else {
            List<BookImage> existingThumbnails = bookImageRepository.findByBookAndImageType(book, "썸네일");
            if (!existingThumbnails.isEmpty()) {
                thumbnailImageUrl = existingThumbnails.get(0).getImage().getUrl();
            }
        }

        if (bookUpdateHtmlRequestDto.removeDetailImage()) {
            removeExistingImages(book, "상세");
            Image detailImage = imageRepository.save(new Image(defaultImageUrl));
            bookImageRepository.save(new BookImage(book, detailImage, "상세"));
            detailImageUrl = detailImage.getUrl();
        } else if (imageUrlRequestDto.detailImageUrl() != null && !imageUrlRequestDto.detailImageUrl().isBlank()) {
            detailImageUrl = imageUrlRequestDto.detailImageUrl();
            removeExistingImages(book, "상세");
            Image detailImage = imageRepository.save(new Image(detailImageUrl));
            bookImageRepository.save(new BookImage(book, detailImage, "상세"));
        } else {
            List<BookImage> existingDetails = bookImageRepository.findByBookAndImageType(book, "상세");
            if (!existingDetails.isEmpty()) {
                detailImageUrl = existingDetails.get(0).getImage().getUrl();
            }
        }

        return new BookUpdateResultResponseDto(
                book.getBookId(),
                book.getTitle(),
                book.getDescription(),
                book.getPublisher().getName(),
                book.getPublishedDate(),
                book.getIsbn(),
                book.getRetailPrice(),
                book.getSellingPrice(),
                book.isGiftWrappable(),
                book.isActive(),
                book.getRemainQuantity(),
                contributorResponseDtos,
                categoryResponseDto,
                tagResponseDtos,
                thumbnailImageUrl,
                detailImageUrl
        );
    }

    /**
     * 특정 도서에 연결된 기존 이미지를 제거하는 메서드
     *
     * @param book
     * @param imageType 이미지 유형에 해당하는 도서의 기존 이미지를 모두 삭제합니다.
     *                  이미지가 더 이상 다른 도서와 연결되어 있지 않을 경우 이미지 데이터를 완전히 삭제합니다.
     */
    public void removeExistingImages(Book book, String imageType) {
        bookImageRepository.findByBookAndImageType(book, imageType)
                .forEach(existing -> {
                    bookImageRepository.delete(existing);
                    if (!bookImageRepository.existsByImage(existing.getImage())) {
                        imageRepository.delete(existing.getImage());
                    }
                });
    }

    /*
     * 특정 도서를 비활성화하는 메서드
     * @param bookId
     */
    @Transactional
    @Override
    public void deactivateBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("도서를 찾을 수 없습니다."));
        book.deactivate();
    }

    @Override
    @Transactional(readOnly = true)
    public int getBookRemainQuantity(Long bookId) {
        return bookRepository.findRemainQuantityByBookId(bookId).orElseThrow(() -> new BookNotFoundException("도서를 찾을 수 없습니다."));
    }
}