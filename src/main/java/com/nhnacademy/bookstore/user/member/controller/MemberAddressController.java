package com.nhnacademy.bookstore.user.member.controller;

import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import com.nhnacademy.bookstore.user.member.mapper.MemberAddressMapper;
import com.nhnacademy.bookstore.user.member.service.MemberAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 정보 Crud 컨트롤러
 *
 * @author Luha
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/members")
public class MemberAddressController {

    private final MemberAddressService memberAddressService;

    @PostMapping("/{memberId}/address")
    public ResponseEntity<List<MemberAddressResponseDto>> addMemberAddress(@PathVariable long memberId, @RequestBody MemberAddressRequestDto requestDto) {

        List<MemberAddressResponseDto> addresses = memberAddressService.addMemberAddress(memberId, requestDto);

        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{memberId}/addresses")
    public ResponseEntity<List<MemberAddressResponseDto>> getAllMemberAddress(@PathVariable long memberId) {
        List<MemberAddressResponseDto> addresses = memberAddressService.getMemberAddresses(memberId);

        return ResponseEntity.ok(addresses);
    }


}
