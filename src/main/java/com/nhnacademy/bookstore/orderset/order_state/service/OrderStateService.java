package com.nhnacademy.bookstore.orderset.order_state.service;

import com.nhnacademy.bookstore.orderset.order_state.repository.OrderStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStateService {

    private final OrderStateRepository orderStateRepository;


}
