package com.nhnacademy.bookstore.bookset.category.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * 카테고리 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
@EqualsAndHashCode
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean isActive;

    public void deactivate() {
        this.isActive = false;
        this.parentCategory = null;
    }

    public void activate() {
        this.isActive = true;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
}
