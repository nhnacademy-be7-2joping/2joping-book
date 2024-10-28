package com.nhnacademy.bookstore.user.member.repository;

import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberAddressRepositroy  extends JpaRepository<MemberAddress, Long> {
    List<MemberAddress> findByMember_Id(long memberId);
    int countByMemberId(Long memberId);
    MemberAddress findByMemberIdAndIsDefaultAddressTrue(Long memberId);


}
