package com.nhnacademy.bookstore.user.customer.service;

import com.nhnacademy.bookstore.orderset.order.dto.request.OrderRequest;
import com.nhnacademy.bookstore.user.customer.dto.request.CustomerRegisterRequest;
import com.nhnacademy.bookstore.user.customer.dto.response.CustomerWithMemberStatusResponse;
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

    @Transactional
    public CustomerWithMemberStatusResponse getOrCreateCustomerIfNonMember(Long customerId, OrderRequest orderRequest) {
        Customer customer;
        boolean isMember = false;

        if (customerId == null) {
            // 비회원 주문인 경우
            CustomerRegisterRequest registerRequest = new CustomerRegisterRequest(
                    orderRequest.deliveryInfo().name(),
                    orderRequest.deliveryInfo().phone(),
                    orderRequest.deliveryInfo().email()
            );
            customer = saveCustomer(registerRequest);
        } else {
            customer = customerRepository.findById(customerId).orElse(null);
            isMember = true;
        }

        return new CustomerWithMemberStatusResponse(customer, isMember);
    }
}
