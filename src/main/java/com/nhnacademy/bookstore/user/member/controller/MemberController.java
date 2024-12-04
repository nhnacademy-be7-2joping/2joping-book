package com.nhnacademy.bookstore.user.member.controller;



import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberWithdrawRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.*;
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
    public ResponseEntity<MemberCreateSuccessResponseDto> addMember(
            @Parameter(description = "회원 가입 정보", required = true) @Valid @RequestBody MemberCreateRequestDto requestDto) {


        MemberCreateSuccessResponseDto response = memberService.registerNewMember(requestDto);


        return ResponseEntity.ok(response);

    }

    /**
     * 회원 정보 수정
     *
     * @param customerId 요청 헤더에서 전달받은 회원 고유 ID
     * @param requestDto 수정할 회원 정보 데이터
     * @return 수정된 회원 정보
     */
    @Operation(
            summary = "회원 정보 수정",
            description = "기존 회원의 정보를 업데이트합니다. 수정할 데이터는 JSON 형식으로 요청 본문에 포함되어야 합니다."
    )
    @PutMapping("/update")
    public ResponseEntity<MemberUpdateResponseDto> updateMember(
            @RequestHeader("X-Customer-Id") String customerId,
            @Valid @RequestBody MemberUpdateRequesteDto requestDto) {

        MemberUpdateResponseDto responseDto = memberService.updateMember(Long.parseLong(customerId), requestDto);

        return ResponseEntity.ok(responseDto);

    }

    /**
     * 회원 정보 조회
     *
     * @param customerId 요청 헤더에서 전달받은 회원 고유 ID
     * @return 회원 정보 (이름, 성별, 생년월일, 연락처, 이메일, 닉네임 등)
     */
    @Operation(
            summary = "회원 정보 조회",
            description = "회원의 기본 정보를 조회합니다. 요청 헤더에 'X-Customer-Id'로 회원 ID를 전달해야 합니다."
    )
    @GetMapping
    public ResponseEntity<MemberUpdateResponseDto> memberInfo(
            @RequestHeader("X-Customer-Id") String customerId) {

        MemberUpdateResponseDto responseDto = memberService.getMemberInfo(Long.parseLong(customerId));

        return ResponseEntity.ok(responseDto);

    }

    @PutMapping("/withdraw")
    public ResponseEntity<MemberWithdrawResponseDto> withdrawMember(
            @RequestHeader("X-Customer-Id") String customerId,
            @Valid @RequestBody MemberWithdrawRequesteDto requestDto) {

        MemberWithdrawResponseDto responseDto = memberService.withdrawMember(Long.parseLong(customerId), requestDto);

        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/details")
    public ResponseEntity<List<GetAllMembersResponse>> getAllMembers(
            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero final int page
    ) {
        List<GetAllMembersResponse> responses = memberService.getAllMembers(page);
        return ResponseEntity.ok(responses);
    }

    /**
     * 회원의 포인트 조회
     *
     * @author 이승준
     * @param customerId 회원 아이디
     * @return 회원이 가진 포인트
     */
    @Operation(
            summary = "회원이 가진 포인트를 조회",
            description = "회원이 가진 포인트를 조회한다. 활용시 X-Customer-Id 헤더 추가 필요"
    )
    @GetMapping("/points")
    public ResponseEntity<MemberPointResponse> getPoint(@RequestHeader("X-Customer-Id") Long customerId) {
        MemberPointResponse response = memberService.getPointsOfMember(customerId);
        return ResponseEntity.ok(response);
    }


}
