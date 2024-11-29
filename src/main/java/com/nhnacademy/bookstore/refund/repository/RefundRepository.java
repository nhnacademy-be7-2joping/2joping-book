package com.nhnacademy.bookstore.refund.repository;

import com.nhnacademy.bookstore.refund.entity.RefundHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundHistory, Long>, RefundQuerydslRepository {
}
