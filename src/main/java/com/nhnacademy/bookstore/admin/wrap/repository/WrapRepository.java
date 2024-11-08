package com.nhnacademy.bookstore.admin.wrap.repository;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WrapRepository extends JpaRepository<Wrap, Long> {
    Optional<Wrap> findByName(String name);
}