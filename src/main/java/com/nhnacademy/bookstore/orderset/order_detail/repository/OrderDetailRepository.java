package com.nhnacademy.bookstore.orderset.order_detail.repository;

import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
