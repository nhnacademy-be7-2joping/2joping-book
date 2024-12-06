package com.nhnacademy.bookstore.point.repository;

import com.nhnacademy.bookstore.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findAllByCustomerIdOrderByRegisterDateDesc(Long customerId);
}
