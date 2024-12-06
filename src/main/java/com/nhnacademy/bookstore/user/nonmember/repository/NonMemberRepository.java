package com.nhnacademy.bookstore.user.nonmember.repository;

import com.nhnacademy.bookstore.user.nonmember.entity.NonMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NonMemberRepository extends JpaRepository<NonMember, Long> {
}
