package com.nhnacademy.bookstore.review.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.common.error.exception.review.RatingValueNotValidException;
import com.nhnacademy.bookstore.common.error.exception.review.ReviewAlreadyExistException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.entity.ReviewImage;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import com.nhnacademy.bookstore.imageset.repository.ReviewImageRepository;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.bookstore.point.service.impl.PointServiceImpl;
import com.nhnacademy.bookstore.review.dto.request.*;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewTotalResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import com.nhnacademy.bookstore.review.mapper.ReviewMapper;
import com.nhnacademy.bookstore.review.repository.ReviewRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Mock
    private PointServiceImpl pointService;


    private ReviewCreateRequestDto createRequestDto;
    private ReviewModifyRequestDto modifyRequestDto;
    private ReviewCreateResponseDto createResponseDto;
    private ReviewModifyResponseDto modifyResponseDto;
    private ReviewResponseDto reviewResponseDto;
    private ReviewTotalResponseDto reviewTotalResponseDto;
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
        reviewTotalResponseDto = new ReviewTotalResponseDto(
                1L, 1L, 1L, 1L, 5,"책 제목" ,"제목", "내용", "test-image.jpg",
                Timestamp.valueOf(LocalDateTime.now()), null
        );

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("리뷰 등록 테스트")
    void registerReview() {
        // 필요한 객체 모의 설정
        Book mockBook = mock(Book.class);
        Member mockMember = mock(Member.class);
        OrderDetail mockOrderDetail = mock(OrderDetail.class);
        Review mockReview = mock(Review.class);

        when(orderDetailRepository.findBookIdByOrderDetailId(anyLong())).thenReturn(Optional.of(1L));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mockBook));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mockMember));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(mockOrderDetail));
        when(reviewRepository.existsByOrderDetail_OrderDetailId(anyLong())).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
        when(reviewMapper.toCreateResponseDto(any())).thenReturn(createResponseDto);

        // 이미지 저장 관련 모의 설정
        when(imageRepository.save(any())).thenReturn(mock(Image.class));

        // 테스트 실행
        ReviewCreateResponseDto response = reviewService.registerReview(createRequestDto);

        // 검증
        assertNotNull(response);
        assertEquals(createResponseDto, response); // 객체 동등성 검증

        // PointService의 메서드 호출 여부 검증
        verify(pointService, times(1)).awardReviewPoint(any());
    }


    @Test
    @DisplayName("리뷰 등록 실패 - 이미 존재하는 리뷰")
    void registerReview_reviewAlreadyExists() {
        // Mock 설정: 리뷰가 이미 존재하는 경우
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mock(Member.class)));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(mock(OrderDetail.class)));
        when(orderDetailRepository.findBookIdByOrderDetailId(anyLong())).thenReturn(Optional.of(1L));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class)));
        when(reviewRepository.existsByOrderDetail_OrderDetailId(anyLong())).thenReturn(true);

        // 예외 검증
        assertThrows(ReviewAlreadyExistException.class, () -> reviewService.registerReview(createRequestDto));

        // Repository 호출 검증
        verify(reviewRepository, times(1)).existsByOrderDetail_OrderDetailId(anyLong());
        verify(reviewRepository, never()).save(any()); // 리뷰 저장 호출 X
    }

    @Test
    @DisplayName("리뷰 등록 실패 - 유효하지 않은 평점")
    void registerReview_invalidRatingValue() {
        // Mock 설정
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mock(Member.class)));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(mock(OrderDetail.class)));
        when(orderDetailRepository.findBookIdByOrderDetailId(anyLong())).thenReturn(Optional.of(1L));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class)));
        when(reviewRepository.existsByOrderDetail_OrderDetailId(anyLong())).thenReturn(false);

        // 유효하지 않은 평점을 가진 DTO 생성
        ReviewCreateRequestDto invalidRatingRequestDto = new ReviewCreateRequestDto(
                new ReviewDetailRequestDto(1L, 1L, 6, "제목", "내용"), // 평점 6 (유효 범위 초과)
                new ReviewImageUrlRequestDto("test-image.jpg")
        );

        // 예외 검증
        assertThrows(RatingValueNotValidException.class, () -> reviewService.registerReview(invalidRatingRequestDto));

        // Repository 호출 검증
        verify(reviewRepository, never()).save(any()); // 리뷰 저장 호출 X
    }

//    @Test
//    @DisplayName("리뷰 등록 실패 - 회원이 존재하지 않는 경우")
//    void registerReview_memberNotFound() {
//        // Mock 설정: 회원을 찾을 수 없는 경우
//        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        // 예외 검증
//        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class,
//                () -> reviewService.registerReview(createRequestDto));
//
//        assertEquals("해당 회원이 없습니다.", exception.getMessage());
//
//        // Repository 호출 검증
//        verify(memberRepository, times(1)).findById(anyLong());
//        verifyNoInteractions(orderDetailRepository, bookRepository, reviewRepository); // 다른 Repository 호출 X
//    }



    @Test
    @DisplayName("리뷰 단일 조회 테스트")
    void getReviews() {
        when(reviewRepository.getReviewByReviewId(anyLong())).thenReturn(Optional.of(reviewResponseDto));

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
        Page<ReviewTotalResponseDto> responsePage = new PageImpl<>(Collections.singletonList(reviewTotalResponseDto), pageable, 1);
        when(reviewRepository.getReviewsByCustomerId(any(Pageable.class), anyLong())).thenReturn(responsePage);

        Page<ReviewTotalResponseDto> response = reviewService.getReviewsByCustomerId(pageable, 1L);

        assertNotNull(response);
        assertEquals(responsePage, response); // 객체 동등성 검증
    }

    @Test
    @DisplayName("리뷰 수정 테스트 - 이미지 추가")
    void modifyReview_withImageAddition() {
        // Mock 객체 생성 및 설정
        Review mockReview = mock(Review.class);
        Image mockImage = mock(Image.class);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(mockReview));
        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);
        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
        when(reviewMapper.toModifyResponseDto(any(Review.class))).thenReturn(modifyResponseDto);

        // 수정된 요청 DTO 생성 (이미지 추가)
        modifyRequestDto = new ReviewModifyRequestDto(
                new ReviewModifyDetailRequestDto(1L, 4, "수정된 제목", "수정된 내용"),
                new ReviewImageUrlRequestDto("new-image.jpg"),
                false
        );

        // 실행
        ReviewModifyResponseDto response = reviewService.modifyReview(modifyRequestDto);

        // 검증
        assertNotNull(response);
        assertEquals(modifyResponseDto, response); // 수정된 객체 검증
        verify(reviewImageRepository, times(1)).save(any(ReviewImage.class)); // 이미지 추가 여부 확인
        verify(reviewRepository, times(1)).save(any(Review.class)); // 리뷰 저장 호출 확인
    }

}

