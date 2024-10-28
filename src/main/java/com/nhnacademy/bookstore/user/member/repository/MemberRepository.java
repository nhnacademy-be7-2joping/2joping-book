package com.nhnacademy.bookstore.user.member.repository;

import com.nhnacademy.bookstore.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
