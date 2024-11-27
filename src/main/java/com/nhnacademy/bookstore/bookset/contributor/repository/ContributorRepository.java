package com.nhnacademy.bookstore.bookset.contributor.repository;

import com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorNameRoleResponseDto;
import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    Optional<Contributor> findByName(String name);

    @Query("SELECT new com.nhnacademy.bookstore.bookset.contributor.dto.response.ContributorNameRoleResponseDto(c.name, r.name) " +
            "FROM Contributor c " +
            "JOIN c.contributorRole r " +
            "WHERE c.isActive = true " +
            "ORDER BY r.name ASC, c.name ASC")
    List<ContributorNameRoleResponseDto> findContributorsWithRoles();
}