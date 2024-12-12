package com.nhnacademy.bookstore.bookset.book.repository;

import com.nhnacademy.bookstore.bookset.book.dto.response.*;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.bookset.book.entity.QBookCategory;
import com.nhnacademy.bookstore.bookset.book.entity.QBookContributor;
import com.nhnacademy.bookstore.bookset.category.entity.QCategory;
import com.nhnacademy.bookstore.bookset.contributor.entity.QContributor;
import com.nhnacademy.bookstore.bookset.contributor.entity.QContributorRole;
import com.nhnacademy.bookstore.bookset.tag.entity.QBookTag;
import com.nhnacademy.bookstore.bookset.tag.entity.QTag;
import com.nhnacademy.bookstore.common.error.exception.bookset.category.CategoryIdNullException;
import com.nhnacademy.bookstore.imageset.entity.QBookImage;
import com.nhnacademy.bookstore.imageset.entity.QImage;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.entity.QReview;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
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
    private final QBookTag qBookTag = QBookTag.bookTag;
    private final QTag qTag = QTag.tag;
    private final QBookImage qBookImage = QBookImage.bookImage;
    private final QImage qImage = QImage.image;
    private final QReview qReview = QReview.review;

    QBookImage qBookImageThumbnail = new QBookImage("thumbnail");
    QBookImage qBookImageDetail = new QBookImage("detail");
    QImage qImageThumbnail = new QImage("thumbnailImage");
    QImage qImageDetail = new QImage("detailImage");

    /**
     * 전체 도서를 페이지 단위로 조회
     *
     * @param pageable 페이징 정보를 담고 있는 객체
     * @return 전체 도서 목록과 페이징 정보를 담은 Page 객체
     */
    @Override
    public Page<BookSimpleResponseDto> findAllBooks(Pageable pageable) {

        // 1. Book 엔티티를 페이징하여 가져오고 BookImage 및 Image를 조인합니다.
        List<Tuple> results = from(qBook)
                .leftJoin(qBookImage).on(qBook.bookId.eq(qBookImage.book.bookId).and(qBookImage.imageType.eq("썸네일")))
                .leftJoin(qImage).on(qBookImage.image.imageId.eq(qImage.imageId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(qBook, qImage.url)  // Book과 썸네일 URL을 가져옵니다.
                .fetch();

        // 2. 필요한 데이터를 매핑하면서 DTO로 변환합니다.
        List<BookSimpleResponseDto> booksDto = new ArrayList<>();
        for (Tuple tuple : results) {
            Book book = tuple.get(qBook);
            String thumbnailUrl = tuple.get(qImage.url);

            // 각 Book에 대한 BookSimpleResponseDto 생성
            BookSimpleResponseDto dto = new BookSimpleResponseDto(
                    book.getBookId(),
                    thumbnailUrl != null ? thumbnailUrl : "", // 썸네일 URL이 없으면 기본값 사용
                    book.getTitle(),
                    book.getSellingPrice(),
                    book.getPublisher().getName(),
                    book.getRetailPrice(),
                    book.isActive(),
                    new ArrayList<>(), // 기여자 리스트 초기화
                    new ArrayList<>()  // 카테고리 리스트 빈 리스트로 초기화
            );

            // 2-1. 각 Book에 대해 contributor와 contributor role 조회 및 추가
            List<BookContributorResponseDto> contributors = from(qBookContributor)
                    .leftJoin(qContributor).on(qBookContributor.contributor.contributorId.eq(qContributor.contributorId))
                    .leftJoin(qContributorRole).on(qContributor.contributorRole.contributorRoleId.eq(qContributorRole.contributorRoleId))
                    .where(qBookContributor.book.bookId.eq(book.getBookId()))
                    .select(Projections.constructor(BookContributorResponseDto.class,
                            qContributor.contributorId,
                            qContributor.name,
                            qContributorRole.contributorRoleId,
                            qContributorRole.name))
                    .fetch();

            dto.contributorList().addAll(contributors);

            // 2-2. 각 Book에 대해 category 조회 및 추가
            List<String> categories = from(qBookCategory)
                    .leftJoin(qCategory).on(qBookCategory.category.categoryId.eq(qCategory.categoryId))
                    .where(qBookCategory.book.bookId.eq(book.getBookId()))
                    .select(qCategory.category.name)
                    .fetch();

            dto.categoryList().addAll(categories);

            booksDto.add(dto);
        }

        // 3. 총 개수 계산
        long total = from(qBook).fetchCount();

        // 4. Page 객체 반환
        return new PageImpl<>(booksDto, pageable, total);
    }


    /**
     * 특정 기여자가 참여한 도서를 페이지 단위로 조회
     *
     * @param pageable      페이징 정보를 담고 있는 객체
     * @param contributorId 조회할 기여자의 ID
     * @return 기여자가 참여한 도서 목록과 페이징 정보를 담은 Page 객체
     */
    @Override
    public Page<BookSimpleResponseDto> findBooksByContributorId(Pageable pageable, Long contributorId) {

        // 1. 특정 기여자가 참여한 도서를 Book 기준으로 페이징하여 가져오고 썸네일 URL을 조인하여 가져옵니다.
        List<Tuple> results = from(qBookContributor)
                .join(qBookContributor.book, qBook)
                .leftJoin(qBookImage).on(qBook.bookId.eq(qBookImage.book.bookId).and(qBookImage.imageType.eq("썸네일")))
                .leftJoin(qImage).on(qBookImage.image.imageId.eq(qImage.imageId))
                .where(qBookContributor.contributor.contributorId.eq(contributorId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(qBook, qImage.url)
                .fetch();

        // 2. DTO 변환 및 매핑
        List<BookSimpleResponseDto> booksDto = new ArrayList<>();
        for (Tuple tuple : results) {
            Book book = tuple.get(qBook);
            String thumbnailUrl = tuple.get(qImage.url) != null ? tuple.get(qImage.url) : "default-thumbnail.jpg";

            BookSimpleResponseDto dto = new BookSimpleResponseDto(
                    book.getBookId(),
                    thumbnailUrl,
                    book.getTitle(),
                    book.getSellingPrice(),
                    book.getPublisher().getName(),
                    book.getRetailPrice(),
                    book.isActive(),
                    new ArrayList<>(), // 기여자 리스트 초기화
                    new ArrayList<>() // 카테고리 리스트 빈 리스트로 초기화
            );

            // 추가 정보 매핑 (기여자, 카테고리)
            dto.contributorList().addAll(getContributorsByBook(book.getBookId()));
            dto.categoryList().addAll(getCategoriesByBook(book.getBookId()));

            booksDto.add(dto);
        }

        long total = from(qBookContributor)
                .where(qBookContributor.contributor.contributorId.eq(contributorId))
                .fetchCount();

        return new PageImpl<>(booksDto, pageable, total);
    }

    /**
     * 특정 카테고리에 속하는 도서를 페이지 단위로 조회
     *
     * @param pageable   페이징 정보를 담고 있는 객체
     * @param categoryId 조회할 카테고리의 ID
     * @return 카테고리별 도서 목록과 페이징 정보를 담은 Page 객체
     */
    @Override
    public Page<BookSimpleResponseDto> findBooksByCategoryId(Pageable pageable, Long categoryId) {

        List<Tuple> results = from(qBookCategory)
                .join(qBookCategory.book, qBook)
                .leftJoin(qBookImage).on(qBook.bookId.eq(qBookImage.book.bookId).and(qBookImage.imageType.eq("썸네일")))
                .leftJoin(qImage).on(qBookImage.image.imageId.eq(qImage.imageId))
                .where(qBookCategory.category.categoryId.eq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(qBook, qImage.url)
                .fetch();

        List<BookSimpleResponseDto> booksDto = new ArrayList<>();
        for (Tuple tuple : results) {
            Book book = tuple.get(qBook);
            String thumbnailUrl = tuple.get(qImage.url) != null ? tuple.get(qImage.url) : "default-thumbnail.jpg";

            BookSimpleResponseDto dto = new BookSimpleResponseDto(
                    book.getBookId(),
                    thumbnailUrl,
                    book.getTitle(),
                    book.getSellingPrice(),
                    book.getPublisher().getName(),
                    book.getRetailPrice(),
                    book.isActive(),
                    new ArrayList<>(), // 기여자 리스트 초기화
                    new ArrayList<>() // 카테고리 리스트 빈 리스트로 초기화
            );

            dto.contributorList().addAll(getContributorsByBook(book.getBookId()));
            dto.categoryList().addAll(getCategoriesByBook(book.getBookId()));

            booksDto.add(dto);
        }

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

        Tuple bookTuple = from(qBook)
                .leftJoin(qBookImage).on(qBook.bookId.eq(qBookImage.book.bookId).and(qBookImage.imageType.eq("썸네일")))
                .leftJoin(qImage).on(qBookImage.image.imageId.eq(qImage.imageId))
                .where(qBook.bookId.eq(bookId))
                .select(qBook, qImage.url)
                .fetchOne();

        if (bookTuple == null) {
            return Optional.empty();
        }

        Book book = bookTuple.get(qBook);
        String thumbnailUrl = bookTuple.get(qImage.url) != null ? bookTuple.get(qImage.url) : "default-thumbnail.jpg";

        // BookResponseDto 생성
        BookResponseDto bookResponseDto = new BookResponseDto(
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
                getContributorsByBook(book.getBookId()), // 기여자 리스트
                getCategoriesByBook(book.getBookId()), // 카테고리 리스트
                getTagsByBook(book.getBookId()), // 태그 리스트
                thumbnailUrl,
                getReviewsByBook(book.getBookId()) // 리뷰 리스트 추가
        );

        return Optional.of(bookResponseDto);
    }

    /**
     * 특정 도서의 기여자 정보를 조회하여 반환
     */
    public List<BookContributorResponseDto> getContributorsByBook(Long bookId) {
        return from(qBookContributor)
                .leftJoin(qContributor).on(qBookContributor.contributor.contributorId.eq(qContributor.contributorId))
                .leftJoin(qContributorRole).on(qContributor.contributorRole.contributorRoleId.eq(qContributorRole.contributorRoleId))
                .where(qBookContributor.book.bookId.eq(bookId))
                .select(Projections.constructor(BookContributorResponseDto.class,
                        qContributor.contributorId,
                        qContributor.name,
                        qContributorRole.contributorRoleId,
                        qContributorRole.name))
                .fetch();
    }

    /**
     * 특정 도서의 카테고리 정보를 조회하여 반환
     */
    public List<String> getCategoriesByBook(Long bookId) {
        return from(qBookCategory)
                .leftJoin(qCategory).on(qBookCategory.category.categoryId.eq(qCategory.categoryId))
                .where(qBookCategory.book.bookId.eq(bookId))
                .select(qCategory.category.name)
                .fetch();
    }

    /**
     * 특정 도서의 태그 정보를 조회하여 반환
     */
    public List<BookTagResponseDto> getTagsByBook(Long bookId) {
        return from(qBookTag)
                .leftJoin(qTag).on(qBookTag.tag.tagId.eq(qTag.tagId))
                .where(qBookTag.book.bookId.eq(bookId))
                .select(Projections.constructor(BookTagResponseDto.class,
                        qTag.tagId,
                        qTag.name))
                .fetch();
    }

    /**
     * 최하위 카테고리 Id 기반으로 top, middle, bottom 카테고리 가져오기
     *
     * @param lowestCategoryId
     * @return topCategoryId, middleCategoryId, bottomCategoryId 맵
     */
    public Map<String, Long> getCategoryHierarchy(Long lowestCategoryId) {
        if (lowestCategoryId == null) {
            throw new CategoryIdNullException();
        }

        Long bottomCategoryId = null;
        Long middleCategoryId = null;
        Long topCategoryId = null;

        Long currentCategoryId = lowestCategoryId;
        QCategory qParentCategory = new QCategory("parentCategoryAlias");

        while (currentCategoryId != null) {
            Tuple categoryTuple = from(qCategory)
                    .leftJoin(qCategory.parentCategory, qParentCategory)
                    .where(qCategory.categoryId.eq(currentCategoryId))
                    .select(qCategory.categoryId, qParentCategory.categoryId)
                    .fetchOne();

            if (categoryTuple == null) {
                break;
            }

            if (bottomCategoryId == null) {
                bottomCategoryId = categoryTuple.get(qCategory.categoryId);
            } else if (middleCategoryId == null) {
                middleCategoryId = categoryTuple.get(qCategory.categoryId);
            } else {
                topCategoryId = categoryTuple.get(qCategory.categoryId);
                break;
            }

            currentCategoryId = categoryTuple.get(qParentCategory.categoryId);
        }

        if (middleCategoryId == null && bottomCategoryId != null) {
            middleCategoryId = bottomCategoryId;
            bottomCategoryId = null;
        }

        if (topCategoryId == null && middleCategoryId != null) {
            topCategoryId = middleCategoryId;
            middleCategoryId = null;
        }

        Map<String, Long> categoryHierarchy = new HashMap<>();
        categoryHierarchy.put("topCategoryId", topCategoryId);
        categoryHierarchy.put("middleCategoryId", middleCategoryId);
        categoryHierarchy.put("bottomCategoryId", bottomCategoryId);

        return categoryHierarchy;
    }

    /**
     * 업데이트를 위해 특정 도서의 상세 정보를 조회
     *
     * @param bookId
     * @return 도서의 상세 정보를 담은 BookResponseDto 객체
     */
    @Override
    public Optional<BookUpdateResponseDto> findUpdateBookByBookId(Long bookId) {
        Tuple bookTuple = from(qBook)
                .leftJoin(qBookImageThumbnail).on(
                        qBook.bookId.eq(qBookImageThumbnail.book.bookId)
                                .and(qBookImageThumbnail.imageType.eq("썸네일"))
                )
                .leftJoin(qImageThumbnail).on(
                        qBookImageThumbnail.image.imageId.eq(qImageThumbnail.imageId)
                )
                .leftJoin(qBookImageDetail).on(
                        qBook.bookId.eq(qBookImageDetail.book.bookId)
                                .and(qBookImageDetail.imageType.eq("상세"))
                )
                .leftJoin(qImageDetail).on(
                        qBookImageDetail.image.imageId.eq(qImageDetail.imageId)
                )
                .where(qBook.bookId.eq(bookId))
                .select(qBook, qImageThumbnail.url, qImageDetail.url)
                .fetchOne();

        if (bookTuple == null) {
            return Optional.empty();
        }

        Book book = bookTuple.get(qBook);
        String thumbnailUrl = bookTuple.get(qImageThumbnail.url) != null ? bookTuple.get(qImageThumbnail.url) : "default-thumbnail.jpg";
        String detailUrl = bookTuple.get(qImageDetail.url) != null ? bookTuple.get(qImageDetail.url) : "default-detail.jpg";

        List<Long> categoryIds = from(qBookCategory)
                .join(qBookCategory.category, qCategory)
                .where(qBookCategory.book.bookId.eq(bookId))
                .select(qCategory.categoryId)
                .fetch();

        Long bottomCategoryId = categoryIds.isEmpty() ? null : categoryIds.get(0);

        Map<String, Long> categoryHierarchy = getCategoryHierarchy(bottomCategoryId);

        BookUpdateResponseDto bookUpdateResponseDto = new BookUpdateResponseDto(
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
                getContributorsByBook(book.getBookId()),
                categoryHierarchy.get("topCategoryId"),
                categoryHierarchy.get("middleCategoryId"),
                categoryHierarchy.get("bottomCategoryId"),
                getTagsByBook(book.getBookId()),
                thumbnailUrl,
                detailUrl,
                false,
                false
        );

        return Optional.of(bookUpdateResponseDto);
    }

    /**
     * 특정 도서의 리뷰 정보를 조회하여 반환
     */
    private List<ReviewResponseDto> getReviewsByBook(Long bookId) {
        return from(qReview)
                .where(qReview.book.bookId.eq(bookId))
                .select(Projections.constructor(ReviewResponseDto.class,
                        qReview.reviewId,
                        qReview.orderDetail.orderDetailId,
                        qReview.member.id,
                        qReview.book.bookId,
                        qReview.ratingValue,
                        qReview.title,
                        qReview.text,
                        qReview.imageUrl,
                        qReview.createdAt,
                        qReview.updatedAt
                ))
                .fetch();
    }
}
