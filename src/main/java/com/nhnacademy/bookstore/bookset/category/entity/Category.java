package com.nhnacademy.bookstore.bookset.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Category subCategory;

    @Column(nullable = false, length = 50, unique = true)
    private String categoryName;
}