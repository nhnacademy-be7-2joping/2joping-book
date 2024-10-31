package com.nhnacademy.bookstore.user.member.repository.impl;

import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.member.repository.MemberQuerydslRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberQuerydslRepository {
    public MemberRepositoryImpl(EntityManager entityManager) {
        super(Customer.class);
    }
}
