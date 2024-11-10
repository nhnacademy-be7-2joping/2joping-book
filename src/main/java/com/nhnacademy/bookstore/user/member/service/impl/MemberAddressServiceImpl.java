package com.nhnacademy.bookstore.user.member.service.impl;

import com.nhnacademy.bookstore.common.error.exception.user.address.AddressLimitToTenException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import com.nhnacademy.bookstore.user.member.mapper.MemberAddressMapper;
import com.nhnacademy.bookstore.user.member.repository.MemberAddressRepositroy;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.MemberAddressService;
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
    public List<MemberAddressResponseDto> addMemberAddress(long customerId, MemberAddressRequestDto memberAddressRequestDto) {

        // member 조회 및 예외 처리
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException("멤버" + customerId + "를 찾을 수 없습니다."));

        //주소 개수 조회 10개 제한 /
        int addressCnt = memberAddressRepositroy.countByMemberId(customerId);
        if(addressCnt >= 10) {
            throw new AddressLimitToTenException("주소는 10개까지 저장할 수 있습니다.");
        }

        //기본 배송지 선택시
        if (memberAddressRequestDto.isDefaultAddress()) {

            // 기존에 기본 주소가 설정된 경우 해제
            MemberAddress existingDefaultAddress = memberAddressRepositroy.findByMemberIdAndIsDefaultAddressTrue(customerId);
            if (existingDefaultAddress != null) {
                existingDefaultAddress.setDefaultAddress(false); //defaultAddress  기존에 있던 기본 배송지 해제
            }
        }


        // requestDto -> entity 변환
        MemberAddress address = new MemberAddress();
        address.toEntity(memberAddressRequestDto, member);

        //주소 저장
        memberAddressRepositroy.save(address);
        //변경 후 주소 조회
        List<MemberAddress> memberAddresses = memberAddressRepositroy.findByMember_Id(customerId);

        //dto 변환
        List<MemberAddressResponseDto> memberAddressResponse = new ArrayList<>();

        for (MemberAddress memberAddress : memberAddresses) {
            memberAddressResponse.add(MemberAddressMapper.INSTANCE.toResponseDto(memberAddress));
        }
        //주소 조회
        return memberAddressResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberAddressResponseDto> getMemberAddresses(long customerId) {

        // member 조회 및 예외 처리
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException("멤버" + customerId + "를 찾을 수 없습니다."));
        List<MemberAddressResponseDto> memberAddressResponse = new ArrayList<>();

        //주소 조회
        List<MemberAddress> memberAddresses = memberAddressRepositroy.findByMember_Id(customerId);

        //dto 변환
        for (MemberAddress memberAddress : memberAddresses) {
            memberAddressResponse.add(MemberAddressMapper.INSTANCE.toResponseDto(memberAddress));
        }

        return memberAddressResponse;
    }


}
