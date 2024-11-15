package com.nhnacademy.bookstore.review.service;


import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.bookset.book.BookNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import com.nhnacademy.bookstore.review.mapper.ReviewMapper;
import com.nhnacademy.bookstore.review.repository.ReviewRepository;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
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
                new MemberNotFoundException(("해당 회원이 없습니다."),RedirectType.REDIRECT,"url")); // TODO url 수정

        OrderDetail orderDetail = orderDetailRepository.findById(reviewCreateRequestDto.orderDetailId()).orElseThrow(() ->
        new RuntimeException("에러")); // TODO 예외 처리 따로

        log.debug("orderDetailId: {}", reviewCreateRequestDto.orderDetailId());

        Review review = new Review(
                new Review.ReviewId(reviewCreateRequestDto.orderDetailId()),
                orderDetail,
                customer,
                book,
                reviewCreateRequestDto.title(),
                reviewCreateRequestDto.text(),
                reviewCreateRequestDto.ratingValue(),
                Timestamp.valueOf(LocalDateTime.now()),
                null,
                reviewCreateRequestDto.image() // imageUrl 설정
        );
        log.debug("orderDetailId: {}", reviewCreateRequestDto.orderDetailId());

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toDto(savedReview);
    }
}
