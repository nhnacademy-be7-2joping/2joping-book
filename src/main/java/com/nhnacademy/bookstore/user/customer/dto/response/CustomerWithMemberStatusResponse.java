package com.nhnacademy.bookstore.user.customer.dto.response;

import com.nhnacademy.bookstore.user.customer.entity.Customer;

public record CustomerWithMemberStatusResponse(
        Customer customer,
        Boolean isMember
) {
}
