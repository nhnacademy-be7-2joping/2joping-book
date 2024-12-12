package com.nhnacademy.bookstore.review.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.common.error.exception.review.ReviewAlreadyExistException;
import com.nhnacademy.bookstore.common.error.exception.review.ReviewNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import com.nhnacademy.bookstore.imageset.repository.ReviewImageRepository;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.orderdetail.repository.OrderDetailRepository;
import com.nhnacademy.bookstore.point.dto.request.ReviewPointAwardRequest;
import com.nhnacademy.bookstore.point.service.PointService;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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


@ExtendWith(MockitoExtension.class)
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
    private PointService pointService;

    private ReviewCreateRequestDto createRequestDto;
    private ReviewModifyRequestDto modifyRequestDto;
    private ReviewCreateResponseDto createResponseDto;
    private ReviewModifyResponseDto modifyResponseDto;
    private ReviewResponseDto reviewResponseDto;
    private ReviewTotalResponseDto reviewTotalResponseDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // 필요 객체 초기화
        createRequestDto = new ReviewCreateRequestDto(
                new ReviewDetailRequestDto(1L, 1L, 5, "제목", "내용"),
                new ReviewImageUrlRequestDto("test-image.jpg")
        );

        modifyRequestDto = new ReviewModifyRequestDto(
                new ReviewModifyDetailRequestDto(1L, 4, "수정된 제목", "수정된 내용"),
                new ReviewImageUrlRequestDto("updated-image.jpg"),
                false
        );

        createResponseDto = new ReviewCreateResponseDto(
                1L, 2, "생성된 제목", "생성된 내용", "생성된 리뷰 이미지", Timestamp.valueOf(LocalDateTime.now())
        );

        modifyResponseDto = new ReviewModifyResponseDto(
                1L, 5, "수정된 제목", "수정된 내용", "수정된 리뷰 이미지",
                Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now())
        );

        reviewResponseDto = new ReviewResponseDto(
                1L, 1L, 1L, 1L, 5, "제목", "내용", "test-image.jpg",
                Timestamp.valueOf(LocalDateTime.now()), null
        );

        reviewTotalResponseDto = new ReviewTotalResponseDto(
                1L, 1L, 1L, 1L, 5, "책 제목", "제목", "내용", "test-image.jpg",
                Timestamp.valueOf(LocalDateTime.now()), null
        );

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("리뷰 등록 테스트")
    void registerReview() {
        // Mock 설정
        when(orderDetailRepository.findBookIdByOrderDetailId(anyLong())).thenReturn(Optional.of(1L));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class)));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mock(Member.class)));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(mock(OrderDetail.class)));
        when(reviewRepository.existsByOrderDetail_OrderDetailId(anyLong())).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(mock(Review.class));
        when(reviewMapper.toCreateResponseDto(any())).thenReturn(createResponseDto);
        when(imageRepository.save(any())).thenReturn(mock(Image.class));
        doNothing().when(pointService).awardReviewPoint(any(ReviewPointAwardRequest.class));

        // 실행
        ReviewCreateResponseDto response = reviewService.registerReview(createRequestDto);

        // 검증
        assertNotNull(response);
        assertEquals(createResponseDto, response);
        verify(pointService, times(1)).awardReviewPoint(any());
    }

    @Test
    @DisplayName("리뷰 등록 실패 - 이미 존재하는 리뷰")
    void registerReview_reviewAlreadyExists() {
        // Mock 설정
        when(orderDetailRepository.findBookIdByOrderDetailId(anyLong())).thenReturn(Optional.of(1L));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class)));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mock(Member.class)));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(mock(OrderDetail.class)));
        when(reviewRepository.existsByOrderDetail_OrderDetailId(anyLong())).thenReturn(true);

        // 실행 및 예외 검증
        assertThrows(ReviewAlreadyExistException.class, () -> reviewService.registerReview(createRequestDto));
    }


    @Test
    @DisplayName("리뷰 등록 실패 - 회원이 존재하지 않는 경우")
    void registerReview_MemberNotFoundException() {
        // Given: Mock 설정
        when(orderDetailRepository.findBookIdByOrderDetailId(anyLong())).thenReturn(Optional.of(1L)); // bookId 반환
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mock(Book.class))); // book을 찾을 수 있게 설정
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty()); // 회원을 찾지 못하도록 설정

        ReviewCreateRequestDto reviewCreateRequestDto = new ReviewCreateRequestDto(
                new ReviewDetailRequestDto(1L, 1L, 5, "Test Title", "Test Text"),
                new ReviewImageUrlRequestDto("test-image.jpg")
        );

        // When & Then: 예외 검증
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class,
                () -> reviewService.registerReview(reviewCreateRequestDto));

        // 예외 메시지 및 Redirect URL 검증
        assertEquals("해당 회원이 없습니다.", exception.getMessage());
        assertEquals("/mypage/mypage", exception.getUrl());

        // 호출 검증: MemberRepository만 호출되고, 이후 단계는 호출되지 않아야 함
        verify(memberRepository, times(1)).findById(anyLong());
        verifyNoInteractions(reviewRepository); // Review 저장은 호출되지 않아야 함
    }




    @Test
    @DisplayName("리뷰 단일 조회 테스트 - 성공")
    void getReview_success() {
        // Mock 설정
        when(reviewRepository.getReviewByReviewId(anyLong())).thenReturn(Optional.of(reviewResponseDto));

        // 실행
        Optional<ReviewResponseDto> response = reviewService.getReviews(new ReviewRequestDto(1L));

        // 검증
        assertTrue(response.isPresent());
        assertEquals(reviewResponseDto, response.get());
        verify(reviewRepository, times(1)).getReviewByReviewId(anyLong());
    }

    @Test
    @DisplayName("리뷰 단일 조회 테스트 - 실패 (존재하지 않는 리뷰)")
    void getReview_notFound() {
        // Mock 설정
        when(reviewRepository.getReviewByReviewId(anyLong())).thenReturn(Optional.empty());

        // 실행 및 검증
        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReviews(new ReviewRequestDto(1L)));
        verify(reviewRepository, times(1)).getReviewByReviewId(anyLong());
    }

    @Test
    @DisplayName("도서별 리뷰 조회 테스트")
    void getReviewsByBookId() {
        // Mock 설정
        Page<ReviewResponseDto> mockPage = new PageImpl<>(Collections.singletonList(reviewResponseDto), pageable, 1);
        when(reviewRepository.getReviewsByBookId(any(Pageable.class), anyLong())).thenReturn(mockPage);

        // 실행
        Page<ReviewResponseDto> response = reviewService.getReviewsByBookId(pageable, 1L);

        // 검증
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(reviewResponseDto, response.getContent().get(0));
        verify(reviewRepository, times(1)).getReviewsByBookId(any(Pageable.class), anyLong());
    }

    @Test
    @DisplayName("회원별 리뷰 조회 테스트")
    void getReviewsByCustomerId() {
        // Mock 설정
        Page<ReviewTotalResponseDto> mockPage = new PageImpl<>(Collections.singletonList(reviewTotalResponseDto), pageable, 1);
        when(reviewRepository.getReviewsByCustomerId(any(Pageable.class), anyLong())).thenReturn(mockPage);

        // 실행
        Page<ReviewTotalResponseDto> response = reviewService.getReviewsByCustomerId(pageable, 1L);

        // 검증
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(reviewTotalResponseDto, response.getContent().get(0));
        verify(reviewRepository, times(1)).getReviewsByCustomerId(any(Pageable.class), anyLong());
    }

    @Test
    @DisplayName("리뷰 수정 테스트 - 제목, 평점, 내용 수정")
    void modifyReview_withTitleRatingTextChange() {
        // Mock 객체 생성 및 설정
        Review mockReview = mock(Review.class);
        Image mockImage = mock(Image.class);

        // 기존 리뷰 Mock 설정
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(mockReview));
        when(mockReview.getImageUrl()).thenReturn(null); // 기존 URL은 null
        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);

        // Mock Image 생성 및 저장 설정
        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);
        when(mockImage.getUrl()).thenReturn("updated-image-url");

        // Mapper Mock 설정
        when(reviewMapper.toModifyResponseDto(any(Review.class))).thenReturn(modifyResponseDto);

        // 실행
        ReviewModifyResponseDto response = reviewService.modifyReview(modifyRequestDto);

        // 검증
        assertNotNull(response);
        assertEquals(modifyResponseDto, response); // 수정된 객체 검증
        verify(imageRepository, times(1)).save(any(Image.class)); // 이미지 저장 여부 확인
        verify(reviewRepository, times(1)).save(any(Review.class)); // 리뷰 저장 호출 확인
    }

    @Test
    @DisplayName("리뷰 수정 테스트 - 실패 (존재하지 않는 리뷰)")
    void modifyReview_notFound() {
        // Mock 설정
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        // 실행 및 검증
        assertThrows(ReviewNotFoundException.class, () -> reviewService.modifyReview(modifyRequestDto));
        verify(reviewRepository, times(1)).findById(anyLong());
        verify(reviewRepository, never()).save(any());
    }


    @Test
    @DisplayName("리뷰 수정 테스트 - 이미지 삭제")
    void modifyReview_withImageDeletion() {
        // Mock 설정
        Review mockReview = mock(Review.class);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(mockReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
        when(reviewMapper.toModifyResponseDto(any(Review.class))).thenReturn(modifyResponseDto);

        // 수정 요청 DTO (이미지 삭제)
        ReviewModifyRequestDto deleteImageDto = new ReviewModifyRequestDto(
                new ReviewModifyDetailRequestDto(1L, 4, "수정된 제목", "수정된 내용"),
                null,
                true
        );

        // 실행
        ReviewModifyResponseDto response = reviewService.modifyReview(deleteImageDto);

        // 검증
        assertNotNull(response);
        assertEquals(modifyResponseDto, response);
        verify(reviewImageRepository, times(1)).deleteByReview_ReviewId(anyLong());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }



}
