package com.nhnacademy.bookstore.bookset.category.repository;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import com.nhnacademy.bookstore.bookset.contributor.entity.ContributorRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 카테고리 레포지토리
 *
 * @author : 정세희
 * @date : 2024-11-07
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentCategory_CategoryId(Long categoryId);
    List<Category> findAllByIsActiveTrue();
    Page<Category> findAllByIsActiveTrue(Pageable pageable);
    Optional<Category> findByName(String name);
    Boolean existsByParentCategory_CategoryId(Long categoryId);
}

