package com.nhnacademy.bookstore.user.member.repository;

import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberAddressRepository  extends JpaRepository<MemberAddress, Long>, MemberAddressQuerydslRepository {

    int countByMemberIdAndAvailableTrue(Long memberId);
    MemberAddress findByMemberIdAndDefaultAddressTrue(Long memberId);
    Optional<MemberAddress> findByMemberIdAndIdAndAvailableTrue(long memberId, long addressId);


}
