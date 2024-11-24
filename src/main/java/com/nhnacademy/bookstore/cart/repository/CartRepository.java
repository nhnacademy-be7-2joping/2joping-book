package com.nhnacademy.bookstore.cart.repository;


import com.nhnacademy.bookstore.cart.entity.Cart;
import com.nhnacademy.bookstore.cart.entity.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {

    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.book b " +
            "JOIN FETCH c.member m " +
            "WHERE c.id.customerId = :customerId")
    List<Cart> findCartsByCustomerId(@Param("customerId")long customerId);

    @Modifying
    @Transactional
    @Query("UPDATE Cart SET quantity = :newQuantity WHERE book.bookId = :bookId")
    void updateQuantity(@Param("bookId") long bookId, @Param("newQuantity") int newQuantity);
}
