package com.nhnacademy.bookstore.bookset.contributor.repository;

import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    Optional<Contributor> findByName(String name);
}