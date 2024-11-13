package com.nhnacademy.bookstore.imageset.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이미지 Entity
 *
 * @author : 이초은
 * @date : 2024-11-12
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "url", nullable = false)
    private String url;

    public Image(String url) {
        this.url = url;
    }
}
