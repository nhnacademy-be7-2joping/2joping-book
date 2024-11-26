package com.nhnacademy.bookstore.user.customer.service;

import com.nhnacademy.bookstore.user.customer.dto.request.CustomerRegisterRequest;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional
    public Customer saveCustomer(CustomerRegisterRequest customerRegisterRequest) {
        Customer customer = customerRepository.findByEmailAndPhone(
                customerRegisterRequest.email(),
                customerRegisterRequest.phone()
        ).orElse(null);

        if (customer == null) {
            customer = new Customer();
            customer.initializeCustomerFields(
                    customerRegisterRequest.name(),
                    customerRegisterRequest.phone(),
                    customerRegisterRequest.email()
            );

            return customerRepository.save(customer);
        } else {
            return customer;
        }
    }

    @Transactional
    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }
}
