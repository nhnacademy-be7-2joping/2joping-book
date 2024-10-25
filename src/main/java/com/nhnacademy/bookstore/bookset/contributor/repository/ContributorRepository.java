package com.nhnacademy.bookstore.bookset.contributor.repository;

import com.nhnacademy.bookstore.bookset.contributor.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributorRepository extends JpaRepository<Contributor, Long> {
}