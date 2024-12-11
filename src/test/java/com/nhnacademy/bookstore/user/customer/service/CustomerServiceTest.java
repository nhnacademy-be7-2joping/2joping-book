package com.nhnacademy.bookstore.user.customer.service;

import com.nhnacademy.bookstore.user.customer.dto.request.CustomerRegisterRequest;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.customer.repository.CustomerRepository;
import com.nhnacademy.bookstore.user.nonmember.service.NonMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * CustomerServiceTest
 * 이 클래스는 CustomerService의 비즈니스 로직을 테스트하여
 * 고객 등록 및 조회 기능이 올바르게 동작하는지 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private NonMemberService nonMemberService;

    private CustomerRegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerRequest = new CustomerRegisterRequest(
                "John Doe",
                "010-1234-5678",
                "john.doe@example.com"
        );
    }


    /**
     * 고객 등록 테스트 - 신규 고객 저장
     * Description: 신규 고객의 정보를 저장하고 저장된 고객 정보를 반환하는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("고객 등록 - 신규 고객 저장")
    void testSaveCustomer_NewCustomer() {
        // Given
        when(customerRepository.findByEmailAndPhone(registerRequest.email(), registerRequest.phone()))
                .thenReturn(Optional.empty());

        Customer newCustomer = new Customer();
        newCustomer.initializeCustomerFields(
                registerRequest.name(),
                registerRequest.phone(),
                registerRequest.email()
        );

        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        // When
        Customer result = customerService.saveCustomer(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals(registerRequest.name(), result.getName());
        assertEquals(registerRequest.phone(), result.getPhone());
        assertEquals(registerRequest.email(), result.getEmail());

        verify(customerRepository).findByEmailAndPhone(registerRequest.email(), registerRequest.phone());
        verify(customerRepository).save(any(Customer.class));
    }

    /**
     * 고객 등록 테스트 - 기존 고객 반환
     * Description: 이미 등록된 고객의 정보를 반환하고 저장 로직이 호출되지 않는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("고객 등록 - 기존 고객 반환")
    void testSaveCustomer_ExistingCustomer() {
        // Given
        Customer existingCustomer = new Customer();
        existingCustomer.initializeCustomerFields(
                registerRequest.name(),
                registerRequest.phone(),
                registerRequest.email()
        );

        when(customerRepository.findByEmailAndPhone(registerRequest.email(), registerRequest.phone()))
                .thenReturn(Optional.of(existingCustomer));

        // When
        Customer result = customerService.saveCustomer(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals(registerRequest.name(), result.getName());
        assertEquals(registerRequest.phone(), result.getPhone());
        assertEquals(registerRequest.email(), result.getEmail());

        verify(customerRepository).findByEmailAndPhone(registerRequest.email(), registerRequest.phone());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    /**
     * 고객 조회 테스트 - 성공
     * Description: 존재하는 고객 ID로 요청 시 예상된 고객 정보를 반환하는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("고객 조회 - 성공")
    void testGetCustomer_Success() {
        // Given
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.initializeCustomerFields(
                registerRequest.name(),
                registerRequest.phone(),
                registerRequest.email()
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // When
        Customer result = customerService.getCustomer(customerId);

        // Then
        assertNotNull(result);
        assertEquals(registerRequest.name(), result.getName());
        assertEquals(registerRequest.phone(), result.getPhone());
        assertEquals(registerRequest.email(), result.getEmail());

        verify(customerRepository).findById(customerId);
    }

    /**
     * 고객 조회 테스트 - 존재하지 않는 고객
     * Description: 존재하지 않는 고객 ID로 요청 시 null이 반환되는지 확인합니다.
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("고객 조회 - 존재하지 않는 고객")
    void testGetCustomer_NotFound() {
        // Given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When
        Customer result = customerService.getCustomer(customerId);

        // Then
        assertNull(result);
        verify(customerRepository).findById(customerId);
    }
}