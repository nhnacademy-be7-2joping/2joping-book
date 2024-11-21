package com.nhnacademy.bookstore.user.member.dto.response;

import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.memberstatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.tier.enums.Tier;

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
        MemberStatus statusName,
        Tier tierName
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
                member.getStatus(),
                member.getTier().getName()
        );
    }
}
