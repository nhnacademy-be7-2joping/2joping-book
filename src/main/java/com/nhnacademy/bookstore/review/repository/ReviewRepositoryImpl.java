package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.entity.QReview;
import com.nhnacademy.bookstore.review.entity.Review;

import com.querydsl.core.types.Projections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;


public class ReviewRepositoryImpl extends QuerydslRepositorySupport implements ReviewRepositoryCustom{

    public ReviewRepositoryImpl() {
        super(Review.class);
    }

    private final QReview qReview = QReview.review;




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
}

