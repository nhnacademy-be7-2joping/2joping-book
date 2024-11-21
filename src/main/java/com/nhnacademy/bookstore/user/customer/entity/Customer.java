package com.nhnacademy.bookstore.user.customer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "customer")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Setter
    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Setter
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    public void initializeCustomerFields(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
