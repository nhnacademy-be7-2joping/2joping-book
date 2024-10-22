package com.nhnacademy.bookstore.contributor_role.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 도서 기여자 역할 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contributor_role")
public class ContributorRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contributorRoleId;

    @Column(nullable = false, length = 50)
    private String name;
}

