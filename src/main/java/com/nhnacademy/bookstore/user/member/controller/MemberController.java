package com.nhnacademy.bookstore.user.member.controller;


import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.GetAllMembersResponse;
import com.nhnacademy.bookstore.user.member.dto.response.MemberCreateSuccessResponseDto;
import com.nhnacademy.bookstore.user.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 회원 정보 Crud 컨트롤러
 *
 * @author Luha
 * @since 1.0
 */
@Tag(name = "회원 관리 API", description = "회원 정보 생성 및 관리 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 신규 회원 생성
     *
     * @param requestDto 회원 가입 정보
     * @return 생성된 회원 정보 (닉네임, 회원 가입 축하 메세지)
     */
    @Operation(summary = "신규 회원 생성", description = "새로운 회원을 등록합니다.")
    @PostMapping
    public ResponseEntity<MemberCreateSuccessResponseDto> addMemberAddress(
            @Parameter(description = "회원 가입 정보", required = true) @Valid @RequestBody MemberCreateRequestDto requestDto){


        MemberCreateSuccessResponseDto response =  memberService.registerNewMember(requestDto);

        return ResponseEntity.ok(response);

    }

    // TODO: 전체 회원 조회 API
    @GetMapping("")
    public ResponseEntity<List<GetAllMembersResponse>> getAllMembers(
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero final int page
    ) {
        List<GetAllMembersResponse> responses = memberService.getAllMembers(page);
        return ResponseEntity.ok(responses);
    }
}
