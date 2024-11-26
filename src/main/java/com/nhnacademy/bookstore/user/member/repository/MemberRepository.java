package com.nhnacademy.bookstore.user.member.repository;

import com.nhnacademy.bookstore.user.member.dto.response.MemberPointResponse;
import com.nhnacademy.bookstore.user.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydslRepository{
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Page<Member> findAllByOrderByNicknameDesc(Pageable pageable);

    Optional<MemberPointResponse> findPointById(Long customerId);
}
