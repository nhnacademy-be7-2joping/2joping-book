package com.nhnacademy.bookstore.bookset.contributor.repository;

import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContributorRoleRepository extends JpaRepository<ContributorRole, Long> {
    Optional<ContributorRole> findByName(String name);
}