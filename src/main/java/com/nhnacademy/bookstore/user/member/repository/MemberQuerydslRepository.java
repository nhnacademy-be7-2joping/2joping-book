package com.nhnacademy.bookstore.user.member.repository;

import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberUpdateResponseDto;
/**
 * 회원 정보를 관리하기 위한 QueryDSL 기반의 커스텀 Repository 인터페이스입니다.
 * <p>회원 정보 조회 및 업데이트 관련 메서드를 정의합니다.</p>
 *
 * @author Luha
 * @since 1.0
 */
public interface MemberQuerydslRepository {
    MemberUpdateResponseDto updateMemberDetails(MemberUpdateRequesteDto dto, long customerId);
    MemberUpdateResponseDto getMemberInfo(long customerId);

}

