package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Review.ReviewId>, ReviewRepositoryCustom {
}
