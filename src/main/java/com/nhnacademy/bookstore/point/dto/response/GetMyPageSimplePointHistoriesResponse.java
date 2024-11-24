package com.nhnacademy.bookstore.point.dto.response;

import com.nhnacademy.bookstore.user.member.entity.Member;

import java.util.List;

public record GetMyPageSimplePointHistoriesResponse(

        int memberPoint,
        List<GetSimplePointHistoriesResponse> getSimplePointHistoriesResponses
) {
    public static GetMyPageSimplePointHistoriesResponse of(
            Member member,
            List<GetSimplePointHistoriesResponse> getSimplePointHistoriesResponses
    ) {
        return new GetMyPageSimplePointHistoriesResponse(
                member.getPoint(),
                getSimplePointHistoriesResponses
        );
    }
}
