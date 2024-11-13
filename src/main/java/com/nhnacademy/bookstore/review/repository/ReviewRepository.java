package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 리뷰 Repository
 *
 * @author : 이유현
 * @date : 2024-10-29
 */

public interface ReviewRepository extends JpaRepository<Review, Long>,ReviewRepositoryCustom {
}
