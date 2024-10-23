package com.nhnacademy.bookstore.bookset.tag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 태그 Entity
 *
 * @author : 양준하
 * @date : 2024-10-22
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false, length = 25)
    private String name;
}