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
import com.nhnacademy.bookstore.common.error.exception.coupon.CouponInvalidException;
import com.nhnacademy.bookstore.common.error.exception.orderset.order.OrderPointInvalidException;
import com.nhnacademy.bookstore.common.error.exception.shipment.ShipmentNotFoundException;
import com.nhnacademy.bookstore.common.error.exception.user.customer.NonMemberValidationFailed;
import com.nhnacademy.bookstore.coupon.dto.response.MemberCouponResponseDto;
import com.nhnacademy.bookstore.coupon.entity.member.MemberCoupon;
import com.nhnacademy.bookstore.coupon.enums.DiscountType;
import com.nhnacademy.bookstore.coupon.repository.member.MemberCouponRepository;
import com.nhnacademy.bookstore.coupon.service.MemberCouponService;
import com.nhnacademy.bookstore.orderset.order.dto.OrderListResponseDto;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderPostRequest;
import com.nhnacademy.bookstore.orderset.order.dto.request.OrderRequest;
import com.nhnacademy.bookstore.orderset.order.entity.Order;
import com.nhnacademy.bookstore.orderset.order.mapper.OrderMapper;
import com.nhnacademy.bookstore.orderset.order.repository.OrderRepository;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.orderdetail.repository.OrderDetailRepository;
import com.nhnacademy.bookstore.orderset.orderstate.entity.OrderState;
import com.nhnacademy.bookstore.orderset.orderstate.service.OrderStateService;
import com.nhnacademy.bookstore.paymentset.paymenthistory.entity.PaymentHistory;
import com.nhnacademy.bookstore.paymentset.paymenthistory.repository.PaymentHistoryRepository;
import com.nhnacademy.bookstore.paymentset.paymentmethod.entity.PaymentMethod;
import com.nhnacademy.bookstore.paymentset.paymentmethod.enums.PaymentMethodType;
import com.nhnacademy.bookstore.paymentset.paymentmethod.exception.PaymentMethodNotFoundException;
import com.nhnacademy.bookstore.paymentset.paymentmethod.repository.PaymentMethodRepository;
import com.nhnacademy.bookstore.paymentset.status.entity.PaymentStatus;
import com.nhnacademy.bookstore.paymentset.status.enums.PaymentStatusType;
import com.nhnacademy.bookstore.paymentset.status.exception.PaymentStatusNotFoundException;
import com.nhnacademy.bookstore.paymentset.status.repository.PaymentStatusRepository;
import com.nhnacademy.bookstore.point.dto.request.OrderPointAwardRequest;
import com.nhnacademy.bookstore.point.dto.request.PointUseRequest;
import com.nhnacademy.bookstore.point.service.PointService;
import com.nhnacademy.bookstore.shipment.dto.request.ShipmentRequestDto;
import com.nhnacademy.bookstore.shipment.dto.response.ShipmentPolicyResponseDto;
import com.nhnacademy.bookstore.shipment.entity.Shipment;
import com.nhnacademy.bookstore.shipment.entity.ShipmentPolicy;
import com.nhnacademy.bookstore.shipment.repository.ShipmentPolicyRepository;
import com.nhnacademy.bookstore.shipment.repository.ShipmentRepository;
import com.nhnacademy.bookstore.user.customer.dto.response.CustomerWithMemberStatusResponse;
import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.customer.service.CustomerService;
import com.nhnacademy.bookstore.user.member.repository.MemberRepository;
import com.nhnacademy.bookstore.user.member.service.MemberService;
import com.nhnacademy.bookstore.user.nonmember.service.NonMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private final CustomerService customerService;
    private final NonMemberService nonMemberService;
    private final MemberService memberService;
    private final MemberCouponService memberCouponService;
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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OrderRequest validateOrderRequest(OrderRequest orderRequest, Long customerId) {
        Map<Long, Integer> bookQuantityMap =
                orderRequest.cartItemList().stream()
                        .collect(Collectors.toMap(
                                OrderRequest.CartItemRequest::bookId,
                                OrderRequest.CartItemRequest::quantity
                        ));
        List<Book> books = bookRepository.findByBookIdIn(bookQuantityMap.keySet());

        // 도서 가격 재계산
        int bookCost = books.stream().mapToInt(b -> {
            int quantity = bookQuantityMap.getOrDefault(b.getBookId(), 0);
            return b.getSellingPrice() * quantity;
        }).sum();

        // 포장 가격 재계산
        List<OrderRequest.WrapItemRequest> wrapItems =
                orderRequest.wrapList().stream().filter(w -> Objects.nonNull(w.wrapId())).toList();

        // key: 포장 ID, value: 카운트된 포장 ID 수
        Map<Long, Integer> wrapCountMap = new HashMap<>();
        for (OrderRequest.WrapItemRequest item : wrapItems) {
            Long wrapId = item.wrapId();
            Integer wrapCount = wrapCountMap.getOrDefault(wrapId, 0);
            wrapCountMap.put(wrapId, wrapCount + 1);
        }
        List<Wrap> wraps = wrapRepository.findByWrapIdIn(wrapCountMap.keySet());

        int wrapCost = wraps.stream()
                .map(w -> w.getWrapPrice() * wrapCountMap.get(w.getWrapId()))
                .reduce(0, Integer::sum);

        // 배송비 결정
        List<ShipmentPolicyResponseDto> shipmentPolicies =
                shipmentPolicyRepository.findActiveShipmentPolicies()
                        .stream()
                        .filter(s -> isMember(customerId) == s.isMemberOnly()).collect(Collectors.toCollection(ArrayList::new));
        int shipmentCost = 0;

        // 배송 정책 최소 적용 가격 기준으로 정렬 및 책정
        Objects.requireNonNull(shipmentPolicies).sort((p1, p2) -> p1.minOrderAmount() - p2.minOrderAmount());
        for (ShipmentPolicyResponseDto dto : shipmentPolicies) {
            if (bookCost >= dto.minOrderAmount()) {
                shipmentCost = dto.shippingFee();
            }
        }

        // 쿠폰 유효성 검사
        validateCouponUsage(orderRequest, customerId);

        // 쿠폰 사용 id와 일치하는 회원의 쿠폰 조회
        int couponDiscount = calculateCouponDiscount(orderRequest, customerId, bookCost);

        // 포인트 유효성 검사
        int totalCost = bookCost + wrapCost + shipmentCost - couponDiscount;
        int point = 0;
        if (isMember(customerId)) {
            if (memberService.getPointsOfMember(customerId).point() < orderRequest.point()) {
                throw new OrderPointInvalidException("보유한 포인트를 초과했습니다.");
            }

            if (totalCost < orderRequest.point()) {
                throw new OrderPointInvalidException(String.format("포인트 %d는 주문 금액보다 많은 포인트입니다.", orderRequest.point()));
            }

            point = orderRequest.point();
        }

        // 포인트 금액 차감
        totalCost -= point;

        return generateOrderRequest(orderRequest, bookCost, shipmentCost, wrapCost, totalCost, couponDiscount);
    }

    @Transactional
    public int calculateCouponDiscount(OrderRequest orderRequest, Long customerId, int bookCost) {
        if (!isMember(customerId)) {
            return 0;
        }

        List<MemberCouponResponseDto> memberCoupons = memberCouponService.getAllMemberCoupons(customerId);
        if (memberCoupons.isEmpty()) {
            return 0;
        }

        Optional<MemberCouponResponseDto> coupon = memberCoupons.stream()
                .filter(c -> c.couponUsageId().equals(orderRequest.couponId()))
                .findFirst();
        // 조회된 쿠폰이 없으면 예외
        if (coupon.isEmpty()) {
            throw new CouponInvalidException("로그인한 회원이 가진 쿠폰이 아닙니다.");
        }

        return coupon.map(m -> calculateCouponDiscount(bookCost, m)).orElse(0);
    }

    private void validateCouponUsage(OrderRequest orderRequest, Long customerId) {
        if (!isMember(customerId) && orderRequest.couponId() > 0) {
            throw new CouponInvalidException("로그인 후 쿠폰을 사용해주십시오.");
        }
    }

    private OrderRequest generateOrderRequest(
            OrderRequest orderRequest,
            int bookCost,
            int shipmentCost,
            int wrapCost,
            int totalCost,
            int couponDiscount) {
        return new OrderRequest(
                orderRequest.cartItemList(),                // 장바구니 항목
                orderRequest.deliveryInfo(),               // 배송 정보
                orderRequest.point(),                      // 포인트
                orderRequest.couponId(),                   // 쿠폰 ID
                orderRequest.wrapList(),                   // 포장 목록
                bookCost,                                  // 도서 비용
                shipmentCost,                              // 배송비
                wrapCost,                                  // 포장비
                totalCost,                                 // 총 비용
                couponDiscount, // 쿠폰 할인
                orderRequest.nonMemberPassword(),
                orderRequest.orderCode()
        );
    }

    /**
     * 주문 성공 시 수행되는 주문 등록
     *
     * @param orderRequest     사용자가 작성한 주문 요청
     * @param orderPostRequest
     * @param customerId
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void registerOrder(OrderRequest orderRequest, OrderPostRequest orderPostRequest, Long customerId) {
        Order order = new Order();
        OrderState orderState = orderStateService.getWaitingState();
        MemberCoupon memberCoupon = null;
        List<MemberCouponResponseDto> memberCoupons = memberCouponService.getAllMemberCoupons(
                isMember(customerId) ? customerId : 0
        );

        CustomerWithMemberStatusResponse customerWithMemberStatusResponse =
                customerService.getOrCreateCustomerIfNonMember(customerId, orderRequest);
        Customer customer = customerWithMemberStatusResponse.customer();

        // 적용된 쿠폰이 있는 경우 쿠폰 가져오기, (해당 회원이 가진 쿠폰만)
        if (orderRequest.couponId() != null && orderRequest.couponId() > 0 &&
                memberCoupons.stream().anyMatch(dto -> dto.couponId().equals(orderRequest.couponId()))) {
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

        // 비회원인 경우 비회원용 비밀번호 등록
        if (!customerWithMemberStatusResponse.isMember()) {
            if (orderRequest.nonMemberPassword() == null || orderRequest.nonMemberPassword().isEmpty()) {
                throw new NonMemberValidationFailed("비밀번호가 유효하지 않습니다.", RedirectType.REDIRECT, "/");
            }
            nonMemberService.setNonMemberPassword(customer, orderRequest.nonMemberPassword());
        }
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

    private boolean isMember(Long customerId) {
        return customerId != null;
    }

    private int calculateCouponDiscount(int bookCost, MemberCouponResponseDto memberCouponResponseDto) {
        Integer usageLimit = memberCouponResponseDto.couponResponseDto().couponPolicyResponseDto().usageLimit();
        Integer maxDiscount = memberCouponResponseDto.couponResponseDto().couponPolicyResponseDto().maxDiscount();
        DiscountType discountType =
                DiscountType.valueOf(memberCouponResponseDto.couponResponseDto().couponPolicyResponseDto().discountType());
        Integer discountValue = memberCouponResponseDto.couponResponseDto().couponPolicyResponseDto().discountValue();
        int discountCost = discountType.equals(DiscountType.PERCENT) ? discountValue * bookCost / 100 : discountValue;

        if (usageLimit != null && bookCost < usageLimit) {
            return discountCost;
        }

        if (maxDiscount != null && maxDiscount < discountCost) {
            discountCost = maxDiscount;
        }

        return discountCost;
    }
}
