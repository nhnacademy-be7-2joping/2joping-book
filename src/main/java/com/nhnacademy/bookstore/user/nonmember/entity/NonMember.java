package com.nhnacademy.bookstore.user.nonmember.entity;

import com.nhnacademy.bookstore.user.customer.entity.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "non_member")
@PrimaryKeyJoinColumn(name = "customer_id")
@NoArgsConstructor
@AllArgsConstructor
public class NonMember extends Customer {

    @Column(nullable = false, length = 255)
    private String password;
}
