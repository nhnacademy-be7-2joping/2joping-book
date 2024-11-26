package com.nhnacademy.bookstore.user.customer.repository;

import com.nhnacademy.bookstore.user.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmailAndPhone(String email, String phone);
}
