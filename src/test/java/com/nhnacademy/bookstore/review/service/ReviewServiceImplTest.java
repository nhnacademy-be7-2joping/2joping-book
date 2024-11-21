package com.nhnacademy.bookstore.review.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import com.nhnacademy.bookstore.imageset.repository.ReviewImageRepository;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.review.dto.request.*;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import com.nhnacademy.bookstore.review.repository.ReviewRepository;
import com.nhnacademy.bookstore.review.mapper.ReviewMapper;
import com.nhnacademy.bookstore.orderset.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ReviewMapper reviewMapper;

    private ReviewCreateRequestDto createRequestDto;
    private ReviewModifyRequestDto modifyRequestDto;
    private ReviewCreateResponseDto createResponseDto;
    private ReviewModifyResponseDto modifyResponseDto;
    private ReviewResponseDto reviewResponseDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createRequestDto = new ReviewCreateRequestDto(
                new ReviewDetailRequestDto(1L, 1L, 5, "제목", "내용"),
                new ReviewImageUrlRequestDto("test-image.jpg")
        );

        modifyRequestDto = new ReviewModifyRequestDto(
                new ReviewModifyDetailRequestDto(1L, 4, "수정된 제목", "수정된 내용"),
                new ReviewImageUrlRequestDto("updated-image.jpg"),
                false
        );

        createResponseDto = new ReviewCreateResponseDto(1L, 2,"생성된 제목","생성된 내용","생성된 리뷰 이미지",Timestamp.valueOf(LocalDateTime.now()));
        modifyResponseDto = new ReviewModifyResponseDto(1L, 5,"수정된 제목","수정된 내용","수정된 리뷰 이미지",Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now()));
        reviewResponseDto = new ReviewResponseDto(
                1L, 1L, 1L, 1L, 5, "제목", "내용", "test-image.jpg",
                Timestamp.valueOf(LocalDateTime.now()), null
        );

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("리뷰 등록 테스트")
    void registerReview() {
        when(orderDetailRepository.findBookIdByOrderDetailId(anyLong())).thenReturn(Optional.of(1L));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class)));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mock(Member.class)));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(mock(OrderDetail.class)));
        when(reviewRepository.save(any(Review.class))).thenReturn(mock(Review.class));
        when(reviewMapper.toCreateResponseDto(any())).thenReturn(createResponseDto);

        ReviewCreateResponseDto response = reviewService.registerReview(createRequestDto);

        assertNotNull(response);
        assertEquals(createResponseDto, response); // 객체 동등성 검증
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    void modifyReview() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(mock(Review.class)));
        doNothing().when(reviewImageRepository).deleteByReview_ReviewId(anyLong());
        when(imageRepository.save(any())).thenReturn(mock(Image.class));
        when(reviewRepository.save(any(Review.class))).thenReturn(mock(Review.class));
        when(reviewMapper.toModifyResponseDto(any())).thenReturn(modifyResponseDto);

        ReviewModifyResponseDto response = reviewService.modifyReview(modifyRequestDto);

        assertNotNull(response);
        assertEquals(modifyResponseDto, response); // 객체 동등성 검증
    }

    @Test
    @DisplayName("리뷰 단일 조회 테스트")
    void getReviews() {
        when(reviewRepository.getReviewByreviewId(anyLong())).thenReturn(Optional.of(reviewResponseDto));

        Optional<ReviewResponseDto> response = reviewService.getReviews(new ReviewRequestDto(1L));

        assertTrue(response.isPresent());
        assertEquals(Optional.of(reviewResponseDto), response); // Optional 객체 동등성 검증
    }

    @Test
    @DisplayName("도서별 리뷰 조회 테스트")
    void getReviewsByBookId() {
        Page<ReviewResponseDto> responsePage = new PageImpl<>(Collections.singletonList(reviewResponseDto), pageable, 1);
        when(reviewRepository.getReviewsByBookId(any(Pageable.class), anyLong())).thenReturn(responsePage);

        Page<ReviewResponseDto> response = reviewService.getReviewsByBookId(pageable, 1L);

        assertNotNull(response);
        assertEquals(responsePage, response); // 객체 동등성 검증
    }

    @Test
    @DisplayName("회원별 리뷰 조회 테스트")
    void getReviewsByCustomerId() {
        Page<ReviewResponseDto> responsePage = new PageImpl<>(Collections.singletonList(reviewResponseDto), pageable, 1);
        when(reviewRepository.getReviewsByCustomerId(any(Pageable.class), anyLong())).thenReturn(responsePage);

        Page<ReviewResponseDto> response = reviewService.getReviewsByCustomerId(pageable, 1L);

        assertNotNull(response);
        assertEquals(responsePage, response); // 객체 동등성 검증
    }
}

