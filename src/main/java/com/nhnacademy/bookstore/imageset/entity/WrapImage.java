package com.nhnacademy.bookstore.imageset.entity;

import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wrap_image")
public class WrapImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_image_id")
    private Long packageImageId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "wrap_id", nullable = false)
    private Wrap wrap;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    public WrapImage(Wrap wrap, Image image) {
        this.wrap = wrap;
        this.image = image;
    }
}