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
import com.nhnacademy.bookstore.imageset.entity.Image;
import com.nhnacademy.bookstore.imageset.entity.ReviewImage;
import com.nhnacademy.bookstore.imageset.repository.ImageRepository;
import com.nhnacademy.bookstore.imageset.repository.ReviewImageRepository;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.orderdetail.repository.OrderDetailRepository;
import com.nhnacademy.bookstore.point.dto.request.ReviewPointAwardRequest;
import com.nhnacademy.bookstore.point.service.PointService;
import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewModifyRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewTotalResponseDto;
import com.nhnacademy.bookstore.review.entity.Review;
import com.nhnacademy.bookstore.review.mapper.ReviewMapper;
import com.nhnacademy.bookstore.review.repository.ReviewRepository;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ReviewMapper reviewMapper;
    private final ImageRepository imageRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final PointService pointService;


    /**
     * 리뷰 등록
     *
     * @param reviewCreateRequestDto 리뷰 생성을 위한 DTO.
     * @return 생성된 리뷰 정보가 포함된 ReviewCreateResponseDto.
     * @throws MemberNotFoundException 지정된 회원을 찾을 수 없는 경우 발생.
     * @throws OrderNotFoundException 지정된 주문 상세를 찾을 수 없는 경우 발생.
     * @throws ReviewAlreadyExistException 동일한 ID의 리뷰가 이미 존재하는 경우 발생.
     * @throws RatingValueNotValidException 평점 값이 1에서 5 사이가 아닌 경우 발생.
     */
    @Override
    public ReviewCreateResponseDto registerReview(ReviewCreateRequestDto reviewCreateRequestDto) {

        Long bookId = orderDetailRepository.findBookIdByOrderDetailId(reviewCreateRequestDto.reviewDetailRequestDto().orderDetailId())
                .orElseThrow();

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("해당 도서를 찾을 수 없습니다."));

        Member member = memberRepository.findById(reviewCreateRequestDto.reviewDetailRequestDto().customerId()).orElseThrow(() ->
                new MemberNotFoundException(("해당 회원이 없습니다."), RedirectType.REDIRECT, "/mypage/mypage"));

        OrderDetail orderDetail = orderDetailRepository.findById(reviewCreateRequestDto.reviewDetailRequestDto().orderDetailId()).orElseThrow(OrderNotFoundException::new);


        if (reviewRepository.existsByOrderDetail_OrderDetailId(reviewCreateRequestDto.reviewDetailRequestDto().orderDetailId())) {
            throw new ReviewAlreadyExistException("리뷰가 이미 존재합니다.");
        }

        int ratingValue = reviewCreateRequestDto.reviewDetailRequestDto().ratingValue();

        String reviewImage = reviewCreateRequestDto.reviewImageUrlRequestDto().reviewImage() != null ? reviewCreateRequestDto.reviewImageUrlRequestDto().reviewImage() : null;


        Review review = new Review(
                null,
                orderDetail,
                member,
                book,
                reviewCreateRequestDto.reviewDetailRequestDto().title(),
                reviewCreateRequestDto.reviewDetailRequestDto().text(),
                ratingValue,
                Timestamp.valueOf(LocalDateTime.now()),
                null,
                reviewImage
        );

        Review savedReview = reviewRepository.save(review);

        pointService.awardReviewPoint(new ReviewPointAwardRequest(member.getId()));

        if (reviewCreateRequestDto.reviewImageUrlRequestDto().reviewImage() != null) {
            Image imageUrl = imageRepository.save(new Image(reviewImage));
            reviewImageRepository.save(new ReviewImage(review,imageUrl));
        }

        return reviewMapper.toCreateResponseDto(savedReview);
    }

    /**
     * 특정 리뷰를 조회
     *
     * @param reviewRequestDto 리뷰 조회를 위한 DTO.
     * @return 조회된 리뷰 정보가 포함된 ReviewResponseDto.
     * @throws ReviewNotFoundException 지정된 ID의 리뷰를 찾을 수 없는 경우 발생.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<ReviewResponseDto> getReviews(ReviewRequestDto reviewRequestDto) {

        return Optional.of(reviewRepository.getReviewByReviewId(reviewRequestDto.reviewId()).orElseThrow(()-> new ReviewNotFoundException("리뷰가 존재하지 않습니다.")));

    }

    /**
     * 특정 도서에 대한 전체 리뷰를 조회
     *
     * @param pageable 페이징 정보를 담은 객체.
     * @param bookId 도서의 ID.
     * @return 조회된 리뷰 목록과 페이징 정보를 포함한 Page<ReviewResponseDto>.
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ReviewResponseDto> getReviewsByBookId(Pageable pageable, Long bookId) {
        return reviewRepository.getReviewsByBookId(pageable,bookId);
    }

    /**
     * 특정 회원이 작성한 전체 리뷰를 조회
     *
     * @param pageable 페이징 정보를 담은 객체.
     * @param customerId 회원의 ID.
     * @return 조회된 리뷰 목록과 페이징 정보를 포함한 Page<ReviewResponseDto>.
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ReviewTotalResponseDto> getReviewsByCustomerId(Pageable pageable, Long customerId) {
        return reviewRepository.getReviewsByCustomerId(pageable,customerId);
    }

    /**
     * 기존 리뷰를 수정
     *
     * @param reviewModifyRequestDto 리뷰 수정을 위한 DTO.
     * @return 수정된 리뷰 정보가 포함된 ReviewModifyResponseDto.
     * @throws ReviewNotFoundException 지정된 ID의 리뷰를 찾을 수 없는 경우 발생.
     * @throws RatingValueNotValidException 평점 값이 1에서 5 사이가 아닌 경우 발생.
     */
    @Override
    public ReviewModifyResponseDto modifyReview(ReviewModifyRequestDto reviewModifyRequestDto) {

        Review review = reviewRepository.findById(reviewModifyRequestDto.reviewModifyDetailRequestDto().reviewId())
                .orElseThrow(() -> new ReviewNotFoundException("리뷰가 존재하지 않습니다."));

        int ratingValue = reviewModifyRequestDto.reviewModifyDetailRequestDto().ratingValue();

        String updatedImageUrl = review.getImageUrl(); // 기존 URL 기본값

        if (reviewModifyRequestDto.deleteImage()) {
            reviewImageRepository.deleteByReview_ReviewId(review.getReviewId());
            updatedImageUrl = null;

        } else if (reviewModifyRequestDto.reviewImageUrlRequestDto().reviewImage() != null) {
            Image newImage = imageRepository.save(new Image(reviewModifyRequestDto.reviewImageUrlRequestDto().reviewImage()));
            reviewImageRepository.save(new ReviewImage(review, newImage));
            updatedImageUrl = newImage.getUrl();
        }

        review.update(
                ratingValue,
                reviewModifyRequestDto.reviewModifyDetailRequestDto().title(),
                reviewModifyRequestDto.reviewModifyDetailRequestDto().text(),
                updatedImageUrl,
                Timestamp.valueOf(LocalDateTime.now())
        );

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toModifyResponseDto(updatedReview);
    }

}

