package com.nhnacademy.bookstore.user.member.service.impl;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.address.AddressLimitToTenException;
import com.nhnacademy.bookstore.common.error.exception.user.address.AddressNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.member.MemberNotFoundException;
import com.nhnacademy.bookstore.user.member.dto.request.AddressUpdateRequestDto;
import com.nhnacademy.bookstore.user.member.dto.request.MemberAddressRequestDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.AddressDeleteResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.AddressUpdateResponseDto;
import com.nhnacademy.bookstore.user.member.dto.response.address.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import com.nhnacademy.bookstore.user.member.repository.MemberAddressRepository;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * MemberAddressServiceImpl
 * 이 클래스는 회원의 주소 정보를 관리하는 서비스 구현체입니다.
 * 회원 주소 추가와 조회 기능을 제공하며, 주소 개수 제한과 기본 배송지 설정 등 특수한 로직을 처리합니다.
 *
 * @author Luha
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class MemberAddressServiceImpl implements MemberAddressService {

    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository ;
    private static final String MEMBER_MYPAGE_ADDRESS_URL = "/mypage/address-list";


    /**
     * 회원의 주소를 추가하는 메서드입니다.
     *
     * @param customerId               회원의 고유 ID
     * @param memberAddressRequestDto   추가할 주소의 정보가 담긴 DTO
     * @return 회원의 모든 주소 정보를 담은 응답 DTO 리스트
     * @throws MemberNotFoundException 회원을 찾을 수 없는 경우 발생
     * @throws AddressLimitToTenException 주소 개수가 10개를 초과할 때 발생
     * @since 1.0
     */
    @Override
    @Transactional
    public List<MemberAddressResponseDto> addMemberAddress(long customerId, MemberAddressRequestDto memberAddressRequestDto) {

        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "멤버" + customerId + "를 찾을 수 없습니다.",
                        RedirectType.REDIRECT,
                        MEMBER_MYPAGE_ADDRESS_URL,
                        memberAddressRequestDto)
                );

        //주소 개수 조회 10개 제한 /
        int addressCnt = memberAddressRepository.countByMemberIdAndAvailableTrue(customerId);
        if(addressCnt >= 10) {
            throw new AddressLimitToTenException(
                    "주소는 10개까지 저장할 수 있습니다.",
                    RedirectType.REDIRECT,
                    MEMBER_MYPAGE_ADDRESS_URL,
                    memberAddressRequestDto
            );
        }

        //기본 배송지 선택시
        if (memberAddressRequestDto.defaultAddress()) {

            // 기존에 기본 주소가 설정된 경우 해제
            MemberAddress existingDefaultAddress = memberAddressRepository.findByMemberIdAndDefaultAddressTrue(customerId);
            if (existingDefaultAddress != null) {
                existingDefaultAddress.setDefaultAddress(false); //defaultAddress  기존에 있던 기본 배송지 해제
            }
        }

        MemberAddress address = new MemberAddress();
        address.toEntity(memberAddressRequestDto, member);

        //주소 저장
        memberAddressRepository.save(address);
        //변경 후 주소 조회

        return memberAddressRepository.findAddressesByMemberId(customerId);
    }

    /**
     * 회원의 모든 주소를 조회하는 메서드입니다.
     *
     * @param customerId 회원의 고유 ID
     * @return 회원의 모든 주소 정보를 담은 응답 DTO 리스트
     * @throws MemberNotFoundException 회원을 찾을 수 없는 경우 발생
     * @since 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public List<MemberAddressResponseDto> getMemberAddresses(long customerId) {

        Member member = memberRepository.findById(customerId).orElseThrow(() -> new MemberNotFoundException(
                "멤버" + customerId + "를 찾을 수 없습니다.", RedirectType.REDIRECT, MEMBER_MYPAGE_ADDRESS_URL));

        return memberAddressRepository.findAddressesByMemberId(member.getId());
    }

    @Override
    @Transactional
    public AddressDeleteResponseDto deleteMemberAddress(long customerId, long addressId) {

        MemberAddress memberAddress = memberAddressRepository.findByMemberIdAndIdAndAvailableTrue(customerId, addressId)
                .orElseThrow(() -> new AddressNotFoundException("해당 회원 주소를 찾을 수 없습니다.", RedirectType.REDIRECT, MEMBER_MYPAGE_ADDRESS_URL));

        memberAddress.setAvailable(false);

        return new AddressDeleteResponseDto(addressId, "주소가 성공적으로 삭제되었습니다.");    }

    @Override
    @Transactional
    public AddressUpdateResponseDto updateMemberAddress(long customerId, long addressId, AddressUpdateRequestDto requestDto) {

        MemberAddress memberAddress = memberAddressRepository.findByMemberIdAndIdAndAvailableTrue(customerId, addressId)
                .orElseThrow(() -> new AddressNotFoundException("해당 회원 주소를 찾을 수 없습니다.", RedirectType.REDIRECT, MEMBER_MYPAGE_ADDRESS_URL));
        memberAddress.updateAddress(requestDto);

        return new AddressUpdateResponseDto(addressId, "주소가 성공적으로 수정되었습니다.");
    }


}
