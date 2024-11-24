package com.nhnacademy.bookstore.imageset.entity;


import com.nhnacademy.bookstore.review.entity.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review_image")
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long reviewImageId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    public ReviewImage(Review review, Image image) {
        this.review = review;
        this.image = image;
    }

}
