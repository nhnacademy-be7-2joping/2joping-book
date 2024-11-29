package com.nhnacademy.bookstore.orderset.order.service;

import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.nhnacademy.bookstore.admin.wrap.entity.WrapManage;
import com.nhnacademy.bookstore.admin.wrap.repository.WrapManageRepository;
import com.nhnacademy.bookstore.admin.wrap.repository.WrapRepository;
import com.nhnacademy.bookstore.bookset.book.entity.Book;
import com.nhnacademy.bookstore.bookset.book.repository.BookRepository;
import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.bookset.book.BookNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.bookset.book.BookStockOutException;
import com.nhnacademy.bookstore.common.error.exception.shipment.ShipmentNotFoundException;
import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponRepository;
import com.nhnacademy.bookstore.orderset.order.dto.OrderListResponseDto;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderPostRequest;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderRequest;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order.mapper.OrderMapper;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.bookstore.orderset.order_state.entity.OrderState;
import com.nhnacademy.bookstore.orderset.order_state.service.OrderStateService;
import com.nhnacademy.bookstore.paymentset.payment_history.entity.PaymentHistory;
import com.nhnacademy.bookstore.paymentset.payment_history.repository.PaymentHistoryRepository;
import com.nhnacademy.bookstore.paymentset.payment_method.entity.PaymentMethod;
import com.nhnacademy.bookstore.paymentset.payment_method.enums.PaymentMethodType;
import com.nhnacademy.bookstore.paymentset.payment_method.exception.PaymentMethodNotFoundException;
import com.nhnacademy.bookstore.paymentset.payment_method.repository.PaymentMethodRepository;
import com.nhnacademy.bookstore.paymentset.status.entity.PaymentStatus;
import com.nhnacademy.bookstore.paymentset.status.enums.PaymentStatusType;
import com.nhnacademy.bookstore.paymentset.status.exception.PaymentStatusNotFoundException;
import com.nhnacademy.bookstore.paymentset.status.repository.PaymentStatusRepository;
import com.nhnacademy.bookstore.point.dto.request.OrderPointAwardRequest;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.service.PointService;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import com.nhnacademy.bookstore.shipment.entity.Shipment;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepository;
import com.nhnacademy.bookstore.shipment.repository.ShipmentRepository;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 주문 처리를 위한 서비스
 * redis 주문 정보 임시저장/조회와
 * 주문 관련 CRUD를 제공한다
 *
 * @author 이승준
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    private static final String ORDER_KEY = "order:";

    private final RedisTemplate<Object, Object> redisTemplate;
    private final OrderStateService orderStateService;
    private final PointService pointService;

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final ShipmentPolicyRepository shipmentPolicyRepository;
    private final ShipmentRepository shipmentRepository;
    private final WrapRepository wrapRepository;
    private final WrapManageRepository wrapManageRepository;

    private final OrderMapper orderMapper;

    public void registerOrderOnRedis(OrderRequest orderRequest) {
        // 주문 임시 저장 10분 동안 유효
        redisTemplate.opsForValue().set(ORDER_KEY + orderRequest.orderCode(), orderRequest, 10, TimeUnit.MINUTES);
    }

    public OrderRequest getOrderOnRedis(String orderId) {
        return (OrderRequest) redisTemplate.opsForValue().get(ORDER_KEY + orderId);
    }

    /**
     * 주문 정보를 등록한다. 주문 처리 중
     *
     * @param orderRequest
     * @param orderPostRequest
     * @param customer
     */
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
        List<Long> bookIds = cartItemRequests.stream().map(OrderRequest.CartItemRequest::bookId).toList();
        Map<Long, Book> bookMap =
                bookRepository.findByBookIdIn(bookIds).stream().collect(Collectors.toMap(
                        Book::getBookId,
                        book -> book
                ));
        Map<Long, OrderDetail> orderDetailMap = cartItemRequests.stream()
                .map(cartItem -> {
                    Book book = bookMap.get(cartItem.bookId());
                    if (book == null) {
                        throw new BookNotFoundException();
                    }

                    if (book.getRemainQuantity() < cartItem.quantity()) {
                        throw new BookStockOutException("재고 소진", RedirectType.REDIRECT, "/");
                    }

                    book.decreaseQuantity(cartItem.quantity());
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.apply(savedOrder, book, cartItem.quantity(), cartItem.unitPrice());
                    return Map.entry(book.getBookId(), orderDetail);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        orderDetailRepository.saveAll(orderDetailMap.values());

        // 주문 포장 등록
        List<OrderRequest.WrapItemRequest> validWrapRequests =
                orderRequest.wrapList().stream().filter(w -> Objects.nonNull(w.wrapId())).toList();
        List<Long> wrapIds = validWrapRequests.stream().map(OrderRequest.WrapItemRequest::wrapId).toList();
        Map<Long, Wrap> wrapMap =
                wrapRepository.findByWrapIdIn(wrapIds).stream().collect(Collectors.toMap(
                        Wrap::getWrapId,
                        wrap -> wrap
                ));
        List<WrapManage> wrapManages = validWrapRequests.stream()

                .map(item -> {
                    OrderDetail orderDetail = orderDetailMap.get(item.bookId());
                    Wrap wrap = wrapMap.get(item.wrapId());
                    return new WrapManage(null, wrap, orderDetail);
                }).toList();
        wrapManageRepository.saveAll(wrapManages);

        // 배송 등록
        Shipment shipment = new Shipment();
        ShipmentPolicy shipmentPolicy =
                shipmentPolicyRepository.findById(orderRequest.deliveryInfo().deliveryPolicyId())
                        .orElseThrow(ShipmentNotFoundException::new);
        ShipmentRequestDto shipmentRequestDto = new ShipmentRequestDto(
                null,
                orderRequest.deliveryInfo().deliveryPolicyId(),
                savedOrder.getOrderId(),
                orderRequest.deliveryInfo().requirement(),
                null,
                null,
                null
        );
        shipment.toEntity(shipmentRequestDto, null, shipmentPolicy, savedOrder);
        shipmentRepository.save(shipment);

        // 결제 등록
        PaymentMethodType paymentMethodType = determinePaymentMethod(orderPostRequest.method());
        PaymentMethod paymentMethod = paymentMethodRepository.findByPaymentMethodType(paymentMethodType)
                .orElseThrow(
                        () -> new PaymentMethodNotFoundException(orderPostRequest.method()));
        PaymentStatus paymentStatus = paymentStatusRepository.findByStatusType(PaymentStatusType.COMPLETED).orElseThrow(
                PaymentStatusNotFoundException::new);
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.apply(
                paymentStatus,
                paymentMethod,
                savedOrder,
                orderPostRequest.paymentKey(),
                orderRequest.totalCost()
        );
        paymentHistoryRepository.save(paymentHistory);

        // 쿠폰 사용 처리
        Optional.ofNullable(memberCoupon).ifPresent(m -> m.updateUsed(true));

        // 포인트 차감
        memberRepository.findById(customer.getId()).ifPresent(m -> {
            // 회원인 경우만 포인트를 차감한다.
            PointUseRequest pointUseRequest = new PointUseRequest(customer.getId(), orderRequest.point());
            pointService.usePoint(pointUseRequest);
            // 회원 등급별 포인트 적립 수행
            OrderPointAwardRequest orderPointAwardRequest =
                    new OrderPointAwardRequest(
                            customer.getId(),
                            savedOrder.getOrderId()
                    );
            pointService.awardOrderPoint(orderPointAwardRequest);
        });

    }

    /**
     * 정해진 유형 중 결제 유형을 결정하기 위한 메소드
     *
     * @param method 결제 수단
     * @return 결제 수단 유형
     */
    private PaymentMethodType determinePaymentMethod(String method) {
        for (PaymentMethodType type : PaymentMethodType.values()) {
            boolean present = type.getCandidates().stream().anyMatch(c -> c.toUpperCase().contains(method));
            if (present) {
                return type;
            }
        }

        return null;
    }

    public List<OrderListResponseDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toOrderListResponseDto(orders);
    }

    public boolean updateOrderState(Long orderId, Long orderStateId) {
        return orderRepository.updateOrderStateByOrderIdAndOrderStateId(orderId, orderStateId) != 0;
    }
}
