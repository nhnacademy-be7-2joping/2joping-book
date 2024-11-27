package com.nhnacademy.bookstore.review.controller;


import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewModifyRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewTotalResponseDto;
import com.nhnacademy.bookstore.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bookstore/reviews")
@Tag(name = "Review API", description = "리뷰 관련 CRUD API")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록하는 컨트롤러
     * @param reviewCreateRequestDto 리뷰 등록을 위한 dto
     */
    @Operation(summary = "리뷰 등록", description = "새로운 리뷰를 등록합니다.")
    @PostMapping
    public ResponseEntity<ReviewCreateResponseDto> registerReview(@RequestBody @Valid ReviewCreateRequestDto reviewCreateRequestDto, BindingResult bindingResult) {
        ReviewCreateResponseDto responseDto = reviewService.registerReview(reviewCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    /**
     * 리뷰 수정하는 컨트롤러
     * @param reviewModifyRequestDto 리뷰 수정을 위한 dto
     * @return 수정된 리뷰와 상태 코드를 담은 응답
     */
    @Operation(summary = "리뷰 수정", description = "등록된 리뷰를 수정합니다.")
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewModifyResponseDto> modifyReview(@PathVariable Long reviewId,
                                                                @RequestBody @Valid ReviewModifyRequestDto reviewModifyRequestDto,
                                                                BindingResult bindingResult) {

        ReviewModifyResponseDto modifyDto = reviewService.modifyReview(reviewModifyRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(modifyDto);
    }

    /**
     * 리뷰 하나를 조회하는 컨트롤러
     * @param reviewId 리뷰 조회를 위한 reviewId
     * @return 조회할 리뷰와 상태 코드를 담은 응답
     */
    @Operation(summary = "리뷰 조회", description = "등록된 리뷰를 조회합니다.")
    @GetMapping("/{reviewId}")
    public ResponseEntity<Optional<ReviewResponseDto>> getReview(@PathVariable Long reviewId) {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(reviewId);
        Optional<ReviewResponseDto> responseDto = reviewService.getReviews(reviewRequestDto);
        return ResponseEntity.ok(responseDto);

    }

    /**
     * 특정 도서에 달린 리뷰들을 조회하는 컨트롤러
     * @param bookId 리뷰 조회를 위한 bookId
     * @return 조회할 리뷰와 상태 코드를 담은 응답
     */
    @Operation(summary = "도서별 리뷰 조회", description = "특정 도서에 등록된 리뷰들을 조회합니다.")
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByBookId(@PathVariable Long bookId,
                                                                      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReviewResponseDto> responseDtoPage = reviewService.getReviewsByBookId(pageable,bookId);
        return ResponseEntity.ok(responseDtoPage);

    }

    /**
     * 회원이 작성한 리뷰들을 조회하는 컨트롤러
     * @param customerId 리뷰 조회를 위한 customerId
     * @return 조회할 리뷰와 상태 코드를 담은 응답
     */
    @Operation(summary = "회원별 리뷰 조회", description = "특정 회원이 등록한 리뷰들을 조회합니다.")
    @GetMapping("/customer")
    public ResponseEntity<Page<ReviewTotalResponseDto>> getReviewsByCustomerId(@RequestHeader("X-Customer-Id") String customerId,
                                                                               @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReviewTotalResponseDto> responseDtoPage = reviewService.getReviewsByCustomerId(pageable, Long.valueOf(customerId));
        return ResponseEntity.ok(responseDtoPage);

    }
}
