package com.nhnacademy.bookstore.user.memberStatus.repository;

import com.nhnacademy.bookstore.user.memberStatus.entity.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {
}
