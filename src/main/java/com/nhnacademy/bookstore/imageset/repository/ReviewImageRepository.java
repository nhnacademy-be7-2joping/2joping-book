package com.nhnacademy.bookstore.imageset.repository;

import com.nhnacademy.bookstore.imageset.entity.ReviewImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


public interface ReviewImageRepository  extends JpaRepository<ReviewImage, Long> {
    @Modifying
    void deleteByReview_ReviewId(Long reviewId);
}
