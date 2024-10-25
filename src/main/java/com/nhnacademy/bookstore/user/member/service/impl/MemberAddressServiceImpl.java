package com.nhnacademy.bookstore.user.member.service.impl;

import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import com.nhnacademy.bookstore.user.member.mapper.MemberAddressMapper;
import com.nhnacademy.bookstore.user.member.repository.MemberAddressRepositroy;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.MemberAddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberAddressServiceImpl implements MemberAddressService {
    private final MemberRepository memberRepository;
    private final MemberAddressRepositroy memberAddressRepositroy ;

    @Override
    @Transactional
    public List<MemberAddressResponseDto> addMemberAddress(long memberId, MemberAddressRequestDto memberAddressRequestDto) {

        // member 조회 및 예외 처리 -> custom exception 생성필요
        Member member  = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        //주소 개수 조회 10개 이라 제한 repo 생성 필요 초과시 예외처리
        List<MemberAddress> memberAddresses = null;

        // requestDto -> entity 변환
        MemberAddress address = MemberAddressMapper.INSTANCE.toEntity(memberAddressRequestDto);
        address.setMember(member);

        //주소 저장
        memberAddressRepositroy.save(address);

        //주소 조회

        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberAddressResponseDto> getMemberAddresses(long customerId) {

        // member 조회 및 예외 처리 -> custom exception 생성필요

        Member member  = memberRepository.findById(customerId).orElseThrow(EntityNotFoundException::new);


        List<MemberAddress> memberAddresses = memberAddressRepositroy.findByMember_Id(customerId);
        List<MemberAddressResponseDto> memberAddressResponse = new ArrayList<>();
        for (MemberAddress memberAddress : memberAddresses) {
            memberAddressResponse.add(MemberAddressMapper.INSTANCE.toResponseDto(memberAddress));
        }

        return memberAddressResponse;
    }


}
