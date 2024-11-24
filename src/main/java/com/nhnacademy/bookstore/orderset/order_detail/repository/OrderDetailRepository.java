package com.nhnacademy.bookstore.orderset.order_detail.repository;

import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT od.book.bookId FROM OrderDetail od WHERE od.orderDetailId = :orderDetailId")
    Optional<Long> findBookIdByOrderDetailId(@Param("orderDetailId") Long orderDetailId);
}
