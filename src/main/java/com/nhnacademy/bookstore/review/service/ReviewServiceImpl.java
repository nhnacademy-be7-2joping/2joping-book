package com.nhnacademy.bookstore.review.service;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.bookset.book.BookNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.orderset.order.OrderNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.review.RatingValueNotValidException;
import com.nhnacademy.bookstore.common.error.exception.review.ReviewAlreadyExistException;
import com.nhnacademy.bookstore.common.error.exception.review.ReviewNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewModifyRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import com.nhnacademy.bookstore.review.mapper.ReviewMapper;
import com.nhnacademy.bookstore.review.repository.ReviewRepository;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ReviewMapper reviewMapper;


    @Transactional
    @Override
    public ReviewCreateResponseDto registerReview(ReviewCreateRequestDto reviewCreateRequestDto) {
        Book book = bookRepository.findById(reviewCreateRequestDto.bookId()).orElseThrow(() ->
                new BookNotFoundException("해당 도서가 없습니다."));

        Customer customer = memberRepository.findById(reviewCreateRequestDto.customerId()).orElseThrow(() ->
                new MemberNotFoundException(("해당 회원이 없습니다."), RedirectType.REDIRECT, "url")); // TODO url 수정

        OrderDetail orderDetail = orderDetailRepository.findById(reviewCreateRequestDto.orderDetailId()).orElseThrow(OrderNotFoundException::new);

        boolean isDuplicateReview = reviewRepository.existsById(reviewCreateRequestDto.reviewId());
        if (isDuplicateReview) { throw new ReviewAlreadyExistException("이미 존재하는 리뷰입니다.");}

        int ratingValue = reviewCreateRequestDto.ratingValue();
        if (ratingValue < 1 || ratingValue > 5) {
            throw new RatingValueNotValidException("평점 " + ratingValue + " 은 유효하지 않습니다. 1~5 사이의 값만 입력 가능합니다.", ratingValue);
        }
        Review review = new Review(
                reviewCreateRequestDto.reviewId(),
                orderDetail,
                customer,
                book,
                reviewCreateRequestDto.title(),
                reviewCreateRequestDto.text(),
                ratingValue,
                Timestamp.valueOf(LocalDateTime.now()),
                null,
                reviewCreateRequestDto.imageUrl() // imageUrl 설정
        );

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toCreateResponseDto(savedReview);
    }

    @Override
    public ReviewResponseDto getReviews(ReviewRequestDto reviewRequestDto) {
        Review review = reviewRepository.findById(reviewRequestDto.reviewId()).orElseThrow(()-> new ReviewNotFoundException("리뷰가 존재하지 않습니다."));
        return reviewMapper.toResponseDto(review);
    }

    @Override
    public ReviewModifyResponseDto modifyReview(ReviewModifyRequestDto reviewModifyRequestDto) {

        Review review = reviewRepository.findById(reviewModifyRequestDto.reviewId()).orElseThrow(()-> new ReviewNotFoundException("리뷰가 존재하지 않습니다."));

        // TODO 사용자 관련 예외처리는 어떻게 하지?
        int ratingValue = reviewModifyRequestDto.ratingValue();
        if (ratingValue < 1 || ratingValue > 5) {
            throw new RatingValueNotValidException("평점 " + ratingValue + " 은 유효하지 않습니다. 1~5 사이의 값만 입력 가능합니다.", ratingValue);
        }
        review.update(ratingValue,reviewModifyRequestDto.title(),reviewModifyRequestDto.text(),reviewModifyRequestDto.imageUrl(),Timestamp.valueOf(LocalDateTime.now()));

        Review updatedReview = reviewRepository.save(review);

        return reviewMapper.toModifyResponseDto(updatedReview);
    }
}

