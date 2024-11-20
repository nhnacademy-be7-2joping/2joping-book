package com.nhnacademy.bookstore.bookset.book.service.impl;

import com.nhnacademy.bookstore.bookset.book.dto.request.BookUpdateHtmlRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.request.BookUpdateRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.request.ImageUrlRequestDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookSimpleResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookUpdateResponseDto;
import com.nhnacademy.bookstore.bookset.book.dto.response.BookUpdateResultResponseDto;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.BookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.BookContributor;
import com.nhnacademy.bookstore.bookset.book.repository.BookCategoryRepository;
import com.nhnacademy.bookstore.bookset.book.repository.BookContributorRepository;
import com.nhnacademy.bookstore.bookset.category.dto.response.GetCategoryResponse;
import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRepository;
import com.nhnacademy.bookstore.bookset.contributor.repository.ContributorRoleRepository;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.bookset.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.bookstore.bookset.tag.repository.BookTagRepository;
import com.nhnacademy.bookstore.bookset.tag.repository.TagRepository;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.contributor.ContributorRoleNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.tag.TagNotFoundException;
import com.nhnacademy.bookstore.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.bookstore.bookset.tag.dto.TagResponseDto;
import com.nhnacademy.bookstore.bookset.tag.entity.BookTag;
import com.nhnacademy.bookstore.bookset.tag.entity.Tag;
import com.nhnacademy.bookstore.common.error.exception.bookset.book.BookNotFoundException;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.bookset.book.service.BookService;
import com.nhnacademy.bookstore.common.error.exception.category.CategoryNotFoundException;
import com.nhnacademy.bookstore.imageset.entity.BookImage;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.repository.BookImageRepository;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final ContributorRepository contributorRepository;
    private final ContributorRoleRepository contributorRoleRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookContributorRepository bookContributorRepository;
    private final CategoryRepository categoryRepository;
    private final BookTagRepository bookTagRepository;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;
    private final BookImageRepository bookImageRepository;

    /**
     * 텍스트를 파싱하여 기여자를 생성하고 반환하는 메서드
     *
     * @param text 파싱할 기여자 텍스트 (이름과 역할을 포함)
     * @return 기여자 리스트 객체 (ContributorResponseDto)
     */
    @Override
    public List<ContributorResponseDto> getContributorList(String text) {
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
                    .orElseThrow(() -> new CategoryNotFoundException("존재하지 않는 카테고리: " + categoryName));

            parentCategory = category;
        }
        return parentCategory;
    }

    /**
     * 텍스트를 파싱하여 도서와 태그를 연관짓는 메서드
     *
     * @param book 태그를 연관시킬 도서 객체
     * @param text 태그 목록 텍스트 (쉼표로 구분된 태그들)
     * @return 태그 리스트 객체 (TagResponseDto)
     */
    public List<TagResponseDto> associateBookWithTag(Book book, String text) {
        List<TagResponseDto> tagResponseDtos = new ArrayList<>();
        String[] splitTags = text.split(",");

        for (String inputTag : splitTags) {
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

    /**
     * 특정 도서를 업데이트용으로 조회하는 메서드
     *
     * @param bookId
     * @return 도서 객체
     */
    @Override
    public BookUpdateResponseDto getUpdateBookByBookId(Long bookId) {
        BookUpdateResponseDto book = bookRepository.findUpdateBookByBookId(bookId).orElseThrow(()-> new BookNotFoundException("도서를 찾을 수 없습니다."));
        return book;
    }

    /**
     * 특정 도서를 업데이트하는 메서드
     *
     * @param bookId
     * @param bookUpdateRequestDto   도서 업데이트 요청 데이터가 담긴 DTO
     * @return 업데이트된 도서의 결과 정보가 담긴 DTO
     */
    @Override
    public BookUpdateResultResponseDto updateBook(Long bookId, BookUpdateRequestDto bookUpdateRequestDto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("책을 찾을 수 없습니다."));

        BookUpdateHtmlRequestDto updateDto = bookUpdateRequestDto.bookUpdateHtmlRequestDto();
        ImageUrlRequestDto imageUrlRequestDto = bookUpdateRequestDto.imageUrlRequestDto();

        Publisher publisher = publisherRepository.findByName(updateDto.publisherName())
                .orElseThrow(() -> new PublisherNotFoundException("출판사를 찾을 수 없습니다."));

        book.updateBook(
                updateDto.title(),
                updateDto.description(),
                publisher,
                updateDto.publishedDate(),
                updateDto.isbn(),
                updateDto.retailPrice(),
                updateDto.sellingPrice(),
                updateDto.giftWrappable(),
                updateDto.isActive(),
                updateDto.remainQuantity()
        );

        bookCategoryRepository.deleteByBook(book);
        Category category = getLowestLevelCategory(updateDto.categoryList());
        bookCategoryRepository.save(new BookCategory(
                new BookCategory.BookCategoryId(book.getBookId(), category.getCategoryId()),
                book,
                category
        ));

        GetCategoryResponse categoryResponseDto = new GetCategoryResponse(
                category.getCategoryId(),
                category.getName(),
                category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null
        );

        bookContributorRepository.deleteByBook(book);
        List<ContributorResponseDto> contributorResponseDtos = getContributorList(updateDto.contributorList());
        contributorResponseDtos.forEach(dto -> {
            Contributor contributor = contributorRepository.findById(dto.contributorId())
                    .orElseThrow(ContributorNotFoundException::new);

            bookContributorRepository.save(new BookContributor(
                    new BookContributor.BookContributorId(book.getBookId(), contributor.getContributorId()),
                    book,
                    contributor
            ));
        });

        bookTagRepository.deleteByBook(book);
        List<TagResponseDto> tagResponseDtos = associateBookWithTag(book, updateDto.tagList());

        String thumbnailImageUrl = null;
        String detailImageUrl = null;

        if (updateDto.removeThumbnailImage()) {
            removeExistingImages(book, "썸네일");
            thumbnailImageUrl = null;
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

        if (updateDto.removeDetailImage()) {
            removeExistingImages(book, "상세");
            detailImageUrl = null;
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
                tagResponseDtos,
                thumbnailImageUrl,
                detailImageUrl
        );
    }

    /**
     * 특정 도서에 연결된 기존 이미지를 제거하는 메서드
     *
     * @param book
     * @param imageType
     *
     * 이미지 유형에 해당하는 도서의 기존 이미지를 모두 삭제합니다.
     * 이미지가 더 이상 다른 도서와 연결되어 있지 않을 경우 이미지 데이터를 완전히 삭제합니다.
     */
    private void removeExistingImages(Book book, String imageType) {
        bookImageRepository.findByBookAndImageType(book, imageType)
                .forEach(existing -> {
                    bookImageRepository.delete(existing);
                    if (!bookImageRepository.existsByImage(existing.getImage())) {
                        imageRepository.delete(existing.getImage());
                    }
                });
    }
}
