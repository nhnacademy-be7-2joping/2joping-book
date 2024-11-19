package com.nhnacademy.bookstore.bookset.category.repository;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
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

    Optional<Category> findByCategoryId(Long categoryId);
    Optional<Category> findByParentCategory(Category parentCategory);
    List<Category> findAllByOrderByNameAsc();
    Optional<Category> findByName(String name);
}
