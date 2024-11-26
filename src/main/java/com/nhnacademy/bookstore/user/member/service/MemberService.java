package com.nhnacademy.bookstore.user.member.service;

import com.nhnacademy.bookstore.user.member.dto.request.MemberCreateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberWithdrawRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MemberService
 * 회원 관련 비즈니스 로직을 담당하는 인터페이스입니다.
 * 신규 회원 등록 등의 기능을 제공합니다.
 *
 * @since 1.0
 * @author Luha
 */
@Service
public interface MemberService {
    MemberCreateSuccessResponseDto registerNewMember(MemberCreateRequestDto memberDto);

    // TODO: 전체 회원 조회 메서드
    List<GetAllMembersResponse> getAllMembers(final int page);
    MemberUpdateResponseDto updateMember(long customerId, MemberUpdateRequesteDto memberDto);
    MemberUpdateResponseDto getMemberInfo(long customerId);
    MemberWithdrawResponseDto withdrawMember(long customerId, MemberWithdrawRequesteDto memberDto);
    MemberPointResponse getPointsOfMember(Long customerId);
}
