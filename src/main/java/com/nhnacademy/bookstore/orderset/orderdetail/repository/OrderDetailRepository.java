package com.nhnacademy.bookstore.orderset.orderdetail.repository;

import com.nhnacademy.bookstore.orderset.orderdetail.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>,OrderDetailRepositoryCustom {
    @Query("SELECT od.book.bookId FROM OrderDetail od WHERE od.orderDetailId = :orderDetailId")
    Optional<Long> findBookIdByOrderDetailId(@Param("orderDetailId") Long orderDetailId);

}
