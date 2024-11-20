package com.nhnacademy.bookstore.cart.repository;


import com.nhnacademy.bookstore.cart.entity.Cart;
import com.nhnacademy.bookstore.cart.entity.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {

    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.id.book b " +
            "JOIN FETCH c.id.member m " +
            "WHERE m.id = :customerId")
    List<Cart> findCartsByCustomerId(@Param("customerId")long customerId);
}
