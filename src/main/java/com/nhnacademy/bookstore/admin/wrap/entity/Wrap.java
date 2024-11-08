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
    @Column(name = "wrap_id")
    private Long wrapId;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "wrap_price")
    @Positive
    private int wrapPrice;

    @Column(name = "is_active")
    private boolean isActive = true;

    public Wrap(String name, int wrapPrice, boolean isActive) {
        this.name = name;
        this.wrapPrice = wrapPrice;
        this.isActive = isActive;
    }

//    public WrapPolicy(String name, int wrapPrice) {
//        this.name = name;
//        this.wrapPrice = wrapPrice;
//    }
//
//    // 포장 정책 수정 메서드
//    public void updatePackagingPolicy(String name, int wrapPrice, boolean isActive) {
//        this.name = name;
//        this.wrapPrice = wrapPrice;
//        this.isActive = isActive;
//    }
}