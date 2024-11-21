package com.nhnacademy.bookstore.user.member.repository;

import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberAddressRepository  extends JpaRepository<MemberAddress, Long>, MemberAddressQuerydslRepository {

    int countByMemberId(Long memberId);
    MemberAddress findByMemberIdAndDefaultAddressTrue(Long memberId);

}
