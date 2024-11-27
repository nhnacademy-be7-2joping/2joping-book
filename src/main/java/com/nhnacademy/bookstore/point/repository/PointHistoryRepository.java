package com.nhnacademy.bookstore.point.repository;

import com.nhnacademy.bookstore.point.entity.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByCustomerId(Long customerId);
    List<PointHistory> findByCustomerIdAndRegisterDateBetween(Long customerId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select sum(p.pointVal) from PointHistory p where p.customerId =: customerId")
    Integer getTotalPointByCustomerId(@Param("customerId") Long customerId);

    Page<PointHistory> findByCustomerIdOrderByRegisterDateDesc(Long customerId, Pageable pageable);
    List<PointHistory> findByCustomerIdOrderByRegisterDateDesc(Long customerId);
}
