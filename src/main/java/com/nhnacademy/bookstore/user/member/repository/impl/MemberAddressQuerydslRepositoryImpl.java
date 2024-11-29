package com.nhnacademy.bookstore.user.member.repository.impl;

import com.nhnacademy.bookstore.user.member.dto.response.address.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.repository.MemberAddressQuerydslRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nhnacademy.bookstore.user.member.entity.QMemberAddress.memberAddress;

@Repository
@RequiredArgsConstructor
public class MemberAddressQuerydslRepositoryImpl implements MemberAddressQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberAddressResponseDto> findAddressesByMemberId(long memberId) {
        return queryFactory
                .select(Projections.constructor(MemberAddressResponseDto.class,
                        memberAddress.id,
                        memberAddress.postalCode,
                        memberAddress.roadAddress,
                        memberAddress.detailAddress,
                        memberAddress.addressAlias,
                        memberAddress.defaultAddress,
                        memberAddress.receiver
                ))
                .from(memberAddress)
                .where(
                        memberAddress.member.id.eq(memberId) // memberId 조건
                                .and(memberAddress.available.isTrue()) // available 필드가 true인 조건 추가
                )
                .orderBy(
                        memberAddress.defaultAddress.desc(), // isDefaultAddress가 true인 항목 먼저
                        memberAddress.id.desc()              // 가장 마지막에 등록된 순서대로
                )
                .fetch();
    }
}
