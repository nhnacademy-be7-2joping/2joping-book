package com.nhnacademy.bookstore.review.controller;


import com.nhnacademy.bookstore.review.dto.request.ReviewCreateRequestDto;
import com.nhnacademy.bookstore.review.dto.request.ReviewModifyRequestDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewCreateResponseDto;
import com.nhnacademy.bookstore.review.dto.response.ReviewResponseDto;
import com.nhnacademy.bookstore.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/review")
@Tag(name = "Review API", description = "리뷰 관련 CRUD API")
public class ReviewController {


    // TODO 필수 ->생성,수정,조회
    // TODO 해야하나? -> 유저가 작성한 모든 리뷰 조회, 유저가 작성한 특정 리뷰 조회, 책에 달린 별점의 평균



    private final ReviewService reviewService;

    //생성
    @Operation(summary = "리뷰 등록", description = "새로운 리뷰를 등록합니다.")
    @PostMapping
    public ResponseEntity<ReviewCreateResponseDto> registerReview(@RequestBody @Valid ReviewCreateRequestDto reviewCreateRequestDto) {
        ReviewCreateResponseDto dto = reviewService.registerReview(reviewCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    //수정
    @PutMapping
    public ResponseEntity<ReviewModifyRequestDto> modifyReview(@RequestBody @Valid ReviewModifyRequestDto reviewModifyRequestDto) {
        return null;
    }

    //조회
    @GetMapping("/{orderDetailId}")
    public ResponseEntity<ReviewResponseDto> getReviews(@PathVariable Long OrderDetailId) {
        return null;
    }
}
