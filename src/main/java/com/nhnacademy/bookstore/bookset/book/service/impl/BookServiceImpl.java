package com.nhnacademy.bookstore.bookset.book.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateHtmlRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.request.BookCreateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.request.ImageUrlRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookCreateResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.BookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.BookContributor;
import com.nhnacademy.bookstore.bookset.book.repository.BookCategoryRepository;
import com.nhnacademy.bookstore.bookset.book.repository.BookContributorRepository;
import com.nhnacademy.bookstore.bookset.category.dto.response.CategoryResponseDto;
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
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.publisher.PublisherNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.tag.TagNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CategoryNotFoundException;
import com.nhnacademy.bookstore.imageset.entity.BookImage;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.repository.BookImageRepository;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
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
                    .orElseThrow(ContributorRoleNotFoundException::new);

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

            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new CategoryNotFoundException());

            parentCategory = category;
        }
        return parentCategory;
    }

//    /**
//     * 텍스트를 파싱하여 도서와 태그를 연관짓는 메서드
//     *
//     * @param book 태그를 연관시킬 도서 객체
//     * @param text 태그 목록 텍스트 (쉼표로 구분된 태그들)
//     * @return 태그 리스트 객체 (TagResponseDto)
//     */
//    public List<TagResponseDto> associateBookWithTag(Book book, String text) {
//        List<TagResponseDto> tagResponseDtos = new ArrayList<>();
//        String[] splitTags = text.split(",");
//
//        for (String inputTag : splitTags) {
//            String tagName = inputTag.trim();
//            Tag tag = tagRepository.findByName(tagName)
//                    .orElseThrow(TagNotFoundException::new);
//
//            BookTag bookTag = new BookTag(
//                    new BookTag.BookTagId(book.getBookId(), tag.getTagId()),
//                    book,
//                    tag
//            );
//            bookTagRepository.save(bookTag);
//
//            tagResponseDtos.add(new TagResponseDto(tag.getTagId(), tag.getName()));
//        }
//
//        return tagResponseDtos;
//    }

    public List<TagResponseDto> associateBookWithTag(Book book, List<String> tagList) {
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

//    @Override
//    public List<ContributorResponseDto> getContributorList(List<Map<String, String>> contributorList) {
//        List<ContributorResponseDto> contributorDtos = new ArrayList<>();
//
//        for (Map<String, String> contributorMap : contributorList) {
//            String name = contributorMap.get("name");
//            String roleName = contributorMap.get("role");
//
//            if (name == null || roleName == null) {
//                throw new IllegalArgumentException();
//            }
//
//            ContributorRole role = contributorRoleRepository.findByName(roleName)
//                    .orElseThrow(ContributorRoleNotFoundException::new);
//
//            Contributor contributor = contributorRepository.findByName(name)
//                    .orElseGet(() -> {
//                        Contributor newContributor = new Contributor(null, role, name, true);
//                        return contributorRepository.save(newContributor);
//                    });
//
//            contributorDtos.add(new ContributorResponseDto(
//                    contributor.getContributorId(),
//                    contributor.getContributorRole().getContributorRoleId(),
//                    contributor.getName()
//            ));
//        }
//
//        return contributorDtos;
//    }

    @Override
    public List<ContributorResponseDto> getContributorList(String contributorListJson) {
        List<ContributorResponseDto> contributorDtos = new ArrayList<>();

        // contributorListJson이 비어 있으면 바로 반환
        if (contributorListJson == null || contributorListJson.isEmpty()) {
            return contributorDtos;
        }

        try {
            // JSON 문자열을 List<Map<String, String>>으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> contributorList = objectMapper.readValue(contributorListJson, new TypeReference<>() {
            });

            // contributorList를 순회하며 처리
            for (Map<String, String> contributorMap : contributorList) {
                String name = contributorMap.get("name");
                String roleName = contributorMap.get("role");

                if (name == null || roleName == null) {
                    throw new IllegalArgumentException("Contributor name or role is missing");
                }

                // ContributorRole을 찾거나, 없으면 예외 처리
                ContributorRole role = contributorRoleRepository.findByName(roleName)
                        .orElseThrow(() -> new ContributorRoleNotFoundException());

                // Contributor를 찾거나 없으면 새로 생성하여 저장
                Contributor contributor = contributorRepository.findByName(name)
                        .orElseGet(() -> {
                            Contributor newContributor = new Contributor(null, role, name, true);
                            return contributorRepository.save(newContributor);
                        });

                // Contributor 정보를 DTO로 변환하여 목록에 추가
                contributorDtos.add(new ContributorResponseDto(
                        contributor.getContributorId(),
                        contributor.getContributorRole().getContributorRoleId(),
                        contributor.getName()
                ));
            }
        } catch (Exception ex) {
            // JSON 변환 중 발생한 예외 처리
            ex.printStackTrace();
            throw new RuntimeException("Error processing contributor list JSON");
        }

        return contributorDtos;
    }

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
                0
        );
        bookRepository.save(book);

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
//        List<ContributorResponseDto> contributorResponseDtos = List.of();

        Category category = getCategoryHierarchy(
                bookCreateRequestDto.bookCreateHtmlRequestDto().topCategoryId(),
                bookCreateRequestDto.bookCreateHtmlRequestDto().middleCategoryId(),
                bookCreateRequestDto.bookCreateHtmlRequestDto().bottomCategoryId()
        );

        // Category category = getLowestLevelCategory(bookCreateHtmlRequestDto.category());
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

        String thumbnailImageUrl = imageUrlRequestDto.thumbnailImageUrl() != null ? imageUrlRequestDto.thumbnailImageUrl() : "default-thumbnail-url";
        String detailImageUrl = imageUrlRequestDto.detailImageUrl() != null ? imageUrlRequestDto.detailImageUrl() : "default-detail-url";

        if (imageUrlRequestDto.thumbnailImageUrl() != null) {
            Image thumbnailImage = imageRepository.save(new Image(thumbnailImageUrl));
            bookImageRepository.save(new BookImage(book, thumbnailImage, "썸네일"));
        }

        if (imageUrlRequestDto.detailImageUrl() != null) {
            Image detailImage = imageRepository.save(new Image(detailImageUrl));
            bookImageRepository.save(new BookImage(book, detailImage, "상세"));
        }

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
     * 전체 도서를 조회하는 메서드
     * @return 도서 객체
     */
    @Override
    public Page<BookSimpleResponseDto> getAllBooks(Pageable pageable) {
        Page<BookSimpleResponseDto> books = bookRepository.findAllBooks(pageable);
        return books;
    }

    /**
     * 카테고리로 도서를 조회하는 메서드
     * @param categoryId
     * @return 도서 객체
     */
    @Override
    public Page<BookSimpleResponseDto> getBooksByCategoryId(Pageable pageable, Long categoryId) {
        return bookRepository.findBooksByCategoryId(pageable, categoryId);
    }

    /**
     * 기여자로 도서를 조회하는 메서드
     * @param contributorId
     * @return 도서 객체
     */
    @Override
    public Page<BookSimpleResponseDto> getBooksByContributorId(Pageable pageable, Long contributorId) {
        return bookRepository.findBooksByContributorId(pageable, contributorId);
    }

    /**
     * 특정 도서를 조회하는 메서드
     * @param bookId
     * @return 도서 객체
     */
    @Override
    public BookResponseDto getBookById(Long bookId) {
        BookResponseDto book = bookRepository.findBookByBookId(bookId).orElseThrow(()-> new BookNotFoundException("도서를 찾을 수 없습니다."));
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


}
