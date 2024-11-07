package com.nhnacademy.bookstore.bookset.category.repository;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryId(Long categoryId);
    Optional<Category> findByParentCategory(Category parentCategory);
    List<Category> findAllByOrderByNameAsc();
}
