package com.nhnacademy.bookstore.user.customer.service;

import com.nhnacademy.bookstore.orderset.order.dto.request.OrderRequest;
import com.nhnacademy.bookstore.user.customer.dto.request.CustomerRegisterRequest;
import com.nhnacademy.bookstore.user.customer.dto.response.CustomerWithMemberStatusResponse;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.customer.repository.CustomerRepository;
import com.nhnacademy.bookstore.user.nonmember.service.NonMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final NonMemberService nonMemberService;

    @Transactional
    public Customer getCustomer(Long customerId, OrderRequest orderRequest) {
        if (customerId == null) {
            // 비회원 주문인 경우
            CustomerRegisterRequest registerRequest = new CustomerRegisterRequest(
                    orderRequest.deliveryInfo().name(),
                    orderRequest.deliveryInfo().phone(),
                    orderRequest.deliveryInfo().email()
            );
            return saveCustomer(registerRequest);
        } else {
            return getCustomer(customerId);
        }
    }

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
            customer = customerRepository.save(customer);
            nonMemberService.saveNonMemberWithCustomer(customer);
        }

        return customer;
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

            // 이미 동일 이메일과 전화번호가 존재하는지 확인
            Optional<Customer> customerOptional = customerRepository.findByEmailAndPhone(
                    orderRequest.deliveryInfo().email(),
                    orderRequest.deliveryInfo().phone()
            );

            if (customerOptional.isPresent()) {
                customer = customerOptional.get();
            } else {
                CustomerRegisterRequest registerRequest = new CustomerRegisterRequest(
                        orderRequest.deliveryInfo().name(),
                        orderRequest.deliveryInfo().phone(),
                        orderRequest.deliveryInfo().email()
                );
                customer = saveCustomer(registerRequest);
            }
        } else {
            customer = customerRepository.findById(customerId).orElse(null);
            isMember = true;
        }

        return new CustomerWithMemberStatusResponse(customer, isMember);
    }
}
