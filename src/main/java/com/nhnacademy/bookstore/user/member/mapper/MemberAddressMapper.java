package com.nhnacademy.bookstore.user.member.mapper;

import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MemberAddressMapper
 *
 * 이 인터페이스는 MemberAddress 엔티티를 MemberAddressResponseDto로 매핑하는 Mapper 인터페이스입니다.
 * Spring의 Mapper 컴포넌트로 동작하며, MapStruct 라이브러리를 통해 구현됩니다.
 *
 * @componentModel spring - Spring의 의존성 주입을 지원하는 Mapper 인터페이스로 설정합니다.
 * @source id - MemberAddress 엔티티의 id 필드를 MemberAddressResponseDto의 memberAddressId 필드로 매핑합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface MemberAddressMapper {

    MemberAddressMapper INSTANCE = Mappers.getMapper(MemberAddressMapper.class);

    @Mapping(source = "id",  target = "memberAddressId")
    MemberAddressResponseDto toResponseDto(MemberAddress memberAddress);

}
