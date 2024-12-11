package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.imageset.entity.QImage;
import com.nhnacademy.bookstore.imageset.entity.QReviewImage;
import com.nhnacademy.bookstore.orderset.order.entity.QOrder;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.QOrderDetail;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewTotalResponseDto;
import com.nhnacademy.bookstore.review.entity.QReview;
import com.nhnacademy.bookstore.review.entity.Review;

import com.nhnacademy.bookstore.user.customer.entity.QCustomer;
import com.nhnacademy.bookstore.user.member.entity.QMember;
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
    private final QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final QOrder qOrder = QOrder.order;
    private final QMember qMember = QMember.member;
    private final QCustomer qCustomer = QCustomer.customer;
    private final QBook qBook = QBook.book;



    @Override
    public Page<ReviewResponseDto> getReviewsByBookId(Pageable pageable, Long bookId) {
        List<ReviewResponseDto> content = from(qReview)
                .where(qReview.book.bookId.eq(bookId))
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
    public Page<ReviewTotalResponseDto> getReviewsByCustomerId(Pageable pageable, Long customerId) {
        List<ReviewTotalResponseDto> content = from(qReview)
                .join(qReview.book, qBook) // book과 review를 JOIN
                .where(qReview.member.id.eq(customerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(Projections.constructor(
                        ReviewTotalResponseDto.class,
                        qReview.reviewId,
                        qReview.orderDetail.orderDetailId,
                        qReview.member.id,
                        qReview.book.bookId,
                        qReview.ratingValue,
                        qBook.title,
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
    public Optional<ReviewResponseDto> getReviewByReviewId(Long reviewId) {
        Tuple reviewTuple = from(qReview)
                .leftJoin(qReviewImage).on(qReview.reviewId.eq(qReviewImage.review.reviewId))
                .leftJoin(qImage).on(qReviewImage.image.imageId.eq(qImage.imageId))
                .where(qReview.reviewId.eq(reviewId))
                .orderBy(qReviewImage.reviewImageId.desc())
                .select(qReview, qImage.url)
                .fetchFirst();

        if (reviewTuple == null) {
            return Optional.empty();
        }

        Review review = reviewTuple.get(qReview);
        String imageUrl = reviewTuple.get(qImage.url) != null ? reviewTuple.get(qImage.url) : null;

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

}