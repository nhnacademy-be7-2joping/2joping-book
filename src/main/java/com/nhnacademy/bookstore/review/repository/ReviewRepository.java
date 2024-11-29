package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    boolean existsByOrderDetail_OrderDetailId(Long orderDetailId);

}
