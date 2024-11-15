package com.nhnacademy.bookstore.user.member.mapper;

import com.nhnacademy.bookstore.user.member.dto.response.GetAllMembersResponse;
import com.nhnacademy.bookstore.user.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "statusName", source = "status.status")
    @Mapping(target = "tierName", source = "tier.tierName")
    GetAllMembersResponse toResponse(Member member);
}
