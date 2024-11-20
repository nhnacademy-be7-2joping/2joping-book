package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.user.member.entity.Member;

import java.util.List;

public record GetMyPageDetailPointHistoriesResponse(

        int memberPoint,
        List<GetDetailPointHistoriesResponse> getDetailPointHistoriesResponses
) {
    public static GetMyPageDetailPointHistoriesResponse of(
            Member member,
            List<GetDetailPointHistoriesResponse> getDetailPointHistoriesResponses
    ) {
        return new GetMyPageDetailPointHistoriesResponse(
                member.getPoint(),
                getDetailPointHistoriesResponses
        );
    }
}
