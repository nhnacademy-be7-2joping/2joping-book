package com.nhnacademy.bookstore.orderset.order.service;

import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponRepository;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderPostRequest;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderRequest;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.bookstore.orderset.order_state.entity.OrderState;
import com.nhnacademy.bookstore.orderset.order_state.service.OrderStateService;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final String ORDER_KEY = "order:";

    private final RedisTemplate<Object, Object> redisTemplate;
    private final OrderStateService orderStateService;
    private final BookRepository bookRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public void registerOrderOnRedis(OrderRequest orderRequest) {
        // 주문 임시 저장 10분 동안 유효
        redisTemplate.opsForValue().set(ORDER_KEY + orderRequest.orderCode(), orderRequest, 10, TimeUnit.MINUTES);
    }

    public OrderRequest getOrderOnRedis(String orderId) {
        return (OrderRequest) redisTemplate.opsForValue().get(ORDER_KEY + orderId);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void registerOrder(OrderRequest orderRequest, OrderPostRequest orderPostRequest, Customer customer) {
        Order order = new Order();
        OrderState orderState = orderStateService.getWaitingState();
        MemberCoupon memberCoupon = null;

        // 적용된 쿠폰이 있는 경우 쿠폰 가져오기
        if (orderRequest.couponId() > 0) {
            memberCoupon = memberCouponRepository.findById(orderRequest.couponId()).orElse(null);
        }
        order.apply(orderState, memberCoupon, orderRequest, orderPostRequest, customer);
        Order savedOrder = orderRepository.save(order);
        redisTemplate.delete(ORDER_KEY + orderPostRequest.orderId()); // 임시저장한 주문정보 삭제

        // 주문 상세 등록
        List<OrderRequest.CartItemRequest> cartItemRequests = orderRequest.cartItemList();
        for (OrderRequest.CartItemRequest cartItemRequest : cartItemRequests) {
            Book book = bookRepository.findById(cartItemRequest.bookId()).orElse(null);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.apply(savedOrder, book, cartItemRequest.quantity(), cartItemRequest.unitPrice());
            orderDetailRepository.save(orderDetail);
        }
    }
}
