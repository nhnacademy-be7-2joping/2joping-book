package com.nhnacademy.bookstore.admin.wrappolicy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class WrapPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrap_id")
    private Long wrapId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "wrap_price", nullable = false)
    @Positive
    private int wrapPrice;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public WrapPolicy(String name, int wrapPrice, boolean isActive) {
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