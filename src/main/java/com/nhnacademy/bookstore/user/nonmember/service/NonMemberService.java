package com.nhnacademy.bookstore.user.nonmember.service;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.customer.CustomerNotFoundException;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.customer.repository.CustomerRepository;
import com.nhnacademy.bookstore.user.nonmember.entity.NonMember;
import com.nhnacademy.bookstore.user.nonmember.repository.NonMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NonMemberService {
    private final CustomerRepository customerRepository;
    private final NonMemberRepository nonMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void setNonMemberPassword(Customer customer, String password) {
        if (customerRepository.existsById(customer.getId())) {
            NonMember nonMember = new NonMember();
            nonMember.initializeCustomerFields(customer.getName(), customer.getPhone(), customer.getEmail());
            nonMember.updatePassword(passwordEncoder.encode(password));
            nonMemberRepository.save(nonMember);
        } else {
            throw new CustomerNotFoundException(customer.getId(), RedirectType.REDIRECT, "/");
        }
    }
}
