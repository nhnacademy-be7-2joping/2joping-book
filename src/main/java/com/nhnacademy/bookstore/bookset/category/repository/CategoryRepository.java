package com.nhnacademy.bookstore.bookset.category.repository;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL AND c.isActive = true")
    List<Category> findTopCategories();

    Optional<Category> findByName(String name);

    Boolean existsByParentCategory_CategoryId(Long categoryId);
}

