package com.nhnacademy.bookstore.review.controller;


import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewModifyRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewModifyResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reviews")
@Tag(name = "Review API", description = "리뷰 관련 CRUD API")
public class ReviewController {


    // TODO 필수 ->생성,수정,조회
    // TODO 해야하나? -> 유저가 작성한 모든 리뷰 조회, 유저가 작성한 특정 리뷰 조회, 책에 달린 별점의 평균

    

    private final ReviewService reviewService;

    //생성
    @Operation(summary = "리뷰 등록", description = "새로운 리뷰를 등록합니다.")
    @PostMapping
    public ResponseEntity<Void> registerReview(@RequestBody @Valid ReviewCreateRequestDto reviewCreateRequestDto) {
        reviewService.registerReview(reviewCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //수정
    @Operation(summary = "리뷰 수정", description = "등록된 리뷰를 수정합니다.")
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewModifyResponseDto> modifyReview(@PathVariable Long reviewId,
                                                                @RequestBody @Valid ReviewModifyRequestDto reviewModifyRequestDto) {
        ReviewModifyResponseDto modifyDto = reviewService.modifyReview(reviewModifyRequestDto);
        return ResponseEntity.ok(modifyDto);
    }

    //조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReviews(@PathVariable Long OrderDetailId) {
        return null;
    }
}
