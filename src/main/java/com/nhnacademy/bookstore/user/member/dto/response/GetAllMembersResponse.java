package com.nhnacademy.bookstore.user.member.dto.response;

import com.nhnacademy.bookstore.user.member.entity.Member;

import java.time.LocalDate;

public record GetAllMembersResponse(

        // TODO: 전체 회원 조회
        Long id,
        String name,
        String loginId,
        String email,
        String phone,
        String nickname,
        LocalDate birthday,
        LocalDate joinDate,
        LocalDate recentLoginDate,
        int point,
        int accPurchase,
        String statusName,
        String tierName
) {
    public static GetAllMembersResponse from(Member member) {
        return new GetAllMembersResponse(
                member.getId(),
                member.getName(),
                member.getLoginId(),
                member.getEmail(),
                member.getPhone(),
                member.getNickname(),
                member.getBirthday(),
                member.getJoinDate(),
                member.getRecentLoginDate(),
                member.getPoint(),
                member.getAccPurchase(),
                member.getStatus().getStatus(),
                member.getTier().getTierName()
        );
    }
}
