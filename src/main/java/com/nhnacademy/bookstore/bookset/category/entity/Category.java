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
    @JoinColumn(name = "subcategory_id")
    private Category subcategory;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Builder
    private Category(
            Category subcategory,
            String name
    ) {
        this.subcategory = subcategory;
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateSubcategory(Category subcategory) {
        this.subcategory = subcategory;
    }
}
