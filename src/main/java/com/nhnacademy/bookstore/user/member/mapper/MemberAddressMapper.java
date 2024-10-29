package com.nhnacademy.bookstore.user.member.mapper;

import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberAddressMapper {

    MemberAddressMapper INSTANCE = Mappers.getMapper(MemberAddressMapper.class);

    @Mapping(source = "id",  target = "memberAddressId")
    MemberAddressResponseDto toResponseDto(MemberAddress memberAddress);


}
