package com.nhnacademy.bookstore.bookset.category.repository;

import com.nhnacademy.bookstore.bookset.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}