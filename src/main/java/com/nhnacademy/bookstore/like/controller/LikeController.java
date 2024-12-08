package com.nhnacademy.bookstore.like.controller;

import com.nhnacademy.bookstore.like.dto.LikeRequestDto;
import com.nhnacademy.bookstore.like.dto.LikeResponseDto;
import com.nhnacademy.bookstore.like.dto.response.MemberLikeResponseDto;
import com.nhnacademy.bookstore.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 태그 Controller
 *
 * @author : 박채연
 * @date : 2024-11-03
 */

@Tag(name = "Like", description = "좋아요 API")
@Validated
@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 책에 좋아요를 설정하거나 취소하는 메서드
     *
     * @param request 좋아요 요청 정보
     * @return LikeResponseDto
     */

    @Operation(summary = "좋아요 설정/취소", description = "해당 책에 좋아요를 설정하거나 취소합니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 설정/취소 성공")
    @ApiResponse(responseCode = "404", description = "회원 또는 책을 찾을 수 없음")
    @PostMapping
    public ResponseEntity<LikeResponseDto> setBookLike(@RequestBody @Valid LikeRequestDto request,
                                                       @RequestHeader("X-Customer-Id") Long customerId) {
        LikeResponseDto responseDto = likeService.setBookLike(request, customerId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 책의 좋아요 개수를 조회하는 메서드
     *
     * @param bookId 책 ID
     * @return Long 좋아요 개수
     */
    @Operation(summary = "책의 좋아요 개수 조회", description = "특정 책의 좋아요 개수를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "책을 찾을 수 없음")
    @GetMapping("/count/{book-id}")
    public ResponseEntity<Long> getLikeCount(@PathVariable("book-id") @Positive Long bookId) {
        Long likeCount = likeService.getLikeCount(bookId);
        return ResponseEntity.ok(likeCount);
    }


    /**
     * 특정 사용자가 좋아요를 누른 책의 목록을 조회하는 메서드
     *
     * @return List<Book> 좋아요를 누른 책 목록
     */
    @Operation(summary = "사용자가 좋아요한 책 목록 조회", description = "특정 사용자가 좋아요를 누른 책 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    @GetMapping("/members")
    public ResponseEntity<List<MemberLikeResponseDto>> getBooksLikedByMember(@RequestHeader("X-Customer-Id") String customerId) {
        List<MemberLikeResponseDto> books = likeService.getBooksLikedByCustomer(Long.parseLong(customerId));
        return ResponseEntity.ok(books);


    }
}



