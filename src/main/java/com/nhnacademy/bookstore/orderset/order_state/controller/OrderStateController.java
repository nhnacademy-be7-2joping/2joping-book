package com.nhnacademy.bookstore.orderset.order_state.controller;

import com.nhnacademy.bookstore.orderset.order_state.repository.OrderStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class OrderStateController {

    private final OrderStateRepository orderStateRepository;


}
