package com.nhnacademy.bookstore.admin.wrap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 포장 정책 Entity
 *
 * @author 박채연
 * @date : 2024-11-05
 */

@Entity
@Getter
@NoArgsConstructor
@Table(name = "wrap")
public class Wrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wrapId;

    @Column(unique = true)
    private String name;

    @Positive
    private int wrapPrice;

    private boolean isActive = true;

    public Wrap(String name, int wrapPrice, boolean isActive) {
        this.name = name;
        this.wrapPrice = wrapPrice;
        this.isActive = isActive;
    }

    public void updateWrap(String name, int wrapPrice, boolean isActive) {
        this.name = name;
        this.wrapPrice = wrapPrice;
        this.isActive = isActive;
    }
}