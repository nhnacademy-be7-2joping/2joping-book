package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.imageset.entity.QImage;
import com.nhnacademy.bookstore.imageset.entity.QReviewImage;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.entity.QReview;
import com.nhnacademy.bookstore.review.entity.Review;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;


public class ReviewRepositoryImpl extends QuerydslRepositorySupport implements ReviewRepositoryCustom{

    public ReviewRepositoryImpl() {
        super(Review.class);
    }

    private final QReview qReview = QReview.review;
    private final QReviewImage qReviewImage = QReviewImage.reviewImage;
    private final QImage qImage = QImage.image;


    @Override
    public Page<ReviewResponseDto> getReviewsByBookId(Pageable pageable, Long bookId) {
        List<ReviewResponseDto> content = from(qReview)
                .where(qReview.book.bookId.eq(bookId)) // bookId로 필터링
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(Projections.constructor(
                        ReviewResponseDto.class,
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

        long total = from(qReview)
                .where(qReview.book.bookId.eq(bookId))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }



    @Override
    public Page<ReviewResponseDto> getReviewsByCustomerId(Pageable pageable, Long customerId) {
        List<ReviewResponseDto> content = from(qReview)
                .where(qReview.member.id.eq(customerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(Projections.constructor(
                        ReviewResponseDto.class,
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

        long total = from(qReview)
                .where(qReview.member.id.eq(customerId))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<ReviewResponseDto> getReviewByreviewId(Long reviewId) {
        // 리뷰와 이미지를 조인하여 최신 이미지를 가져오기
        Tuple reviewTuple = from(qReview)
                .leftJoin(qReviewImage).on(qReview.reviewId.eq(qReviewImage.review.reviewId))
                .leftJoin(qImage).on(qReviewImage.image.imageId.eq(qImage.imageId))
                .where(qReview.reviewId.eq(reviewId))
                .orderBy(qReviewImage.reviewImageId.desc()) // 최신 이미지 우선 정렬
                .select(qReview, qImage.url)
                .fetchFirst(); // 가장 최신의 한 개의 데이터만 가져옴

        if (reviewTuple == null) {
            return Optional.empty();
        }

        // 리뷰 정보 추출
        Review review = reviewTuple.get(qReview);
        String imageUrl = reviewTuple.get(qImage.url) != null ? reviewTuple.get(qImage.url) : "default-image.jpg";

        // ReviewResponseDto 생성
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
                review.getReviewId(),
                review.getOrderDetail().getOrderDetailId(),
                review.getMember().getId(),
                review.getBook().getBookId(),
                review.getRatingValue(),
                review.getTitle(),
                review.getText(),
                imageUrl,
                review.getCreatedAt(),
                review.getUpdatedAt()
        );

        return Optional.of(reviewResponseDto);
    }


    @Override
    public Optional<String> getLatestReviewImageByReviewId(Long reviewId) {
        QReviewImage qReviewImage = QReviewImage.reviewImage;
        QImage qImage = QImage.image;

        // 최신 review_image 데이터를 가져오는 쿼리
        return Optional.ofNullable(from(qReviewImage)
                .leftJoin(qReviewImage.image, qImage)
                .where(qReviewImage.review.reviewId.eq(reviewId))
                .orderBy(qReviewImage.reviewImageId.desc()) // review_image_id 기준 내림차순 정렬
                .select(qImage.url) // URL만 선택
                .fetchFirst());
    }
}


