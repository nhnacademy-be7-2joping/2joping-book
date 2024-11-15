package com.nhnacademy.bookstore.review.repository;

import com.nhnacademy.bookstore.review.entity.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Review customSave(Review review) {
        if (review.getReviewId() == null || review.getReviewId().getOrderDetailId() == null) {
            // ID가 없는 경우 새로 저장 (persist)
            entityManager.persist(review);
            return review;
        } else {
            // ID가 있는 경우 병합 (merge)
            return entityManager.merge(review);
        }
    }
}
