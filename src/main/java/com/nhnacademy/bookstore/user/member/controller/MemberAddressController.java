package com.nhnacademy.bookstore.user.member.controller;

import com.nhnacademy.bookstore.common.annotation.ValidPathVariable;
import com.nhnacademy.bookstore.common.error.exception.user.address.AddressLimitToTenException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.service.MemberAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "회원 주소 관리 API", description = "회원의 주소를 관리하는 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/members")
public class MemberAddressController {

    private final MemberAddressService memberAddressService;

    /**
     * 새로운 주소를 추가하는 기능
     * @author Luha
     * @since 1.0
     *
     * @param memberId 회원 ID
     * @param requestDto 회원 주소 정보 DTO
     * @return 회원의 전체 주소 목록이 포함된 ResponseEntity
     * @throws AddressLimitToTenException 주소 개수가 10개 이상일 경우 발생
     * @throws MemberNotFoundException 회원이 존재하지 않을 경우 발생
     */
    @Operation(summary = "새 주소 추가", description = "특정 회원의 새 주소를 추가합니다. 주소는 최대 10개까지 저장할 수 있습니다.")
    @PostMapping("/{memberId}/address")
    public ResponseEntity<List<MemberAddressResponseDto>> addMemberAddress(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable
            @ValidPathVariable long memberId,
            @Parameter(description = "회원 주소 정보", required = true)
            @Valid
            @RequestBody MemberAddressRequestDto requestDto) {

        List<MemberAddressResponseDto> addresses = memberAddressService.addMemberAddress(memberId, requestDto);

        return ResponseEntity.ok(addresses);
    }

    /**
     * 특정 회원의 모든 주소를 조회
     * @author Luha
     * @since 1.0
     *
     * @param memberId 회원 ID
     * @return 회원 주소 목록이 포함된 ResponseEntity
     * @throws MemberNotFoundException 회원이 존재하지 않을 경우 발생
     */
    @GetMapping("/{memberId}/addresses")
    public ResponseEntity<List<MemberAddressResponseDto>> getAllMemberAddress(
            @Parameter(description = "회원 ID", required = true) @PathVariable long memberId) {

        List<MemberAddressResponseDto> addresses = memberAddressService.getMemberAddresses(memberId);

        return ResponseEntity.ok(addresses);
    }







}
