package com.nhnacademy.bookstore.user.customer.entity;

import com.nhnacademy.bookstore.user.nonmember.entity.NonMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(
        name = "customer",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email", "phone"})
)
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
    @Column(nullable = false, length = 20)
    private String phone;

    @Setter
    @Column(nullable = false, length = 50)
    private String email;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private NonMember nonMember;

    public void initializeCustomerFields(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
