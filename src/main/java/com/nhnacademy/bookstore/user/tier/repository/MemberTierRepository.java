package com.nhnacademy.bookstore.user.tier.repository;

import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTierRepository  extends JpaRepository<MemberTier, Long> {
}
