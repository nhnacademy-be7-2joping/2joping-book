package com.nhnacademy.bookstore.orderset.orderdetail.repository;

import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.orderset.order.entity.QOrder;
import com.nhnacademy.bookstore.orderset.orderdetail.dto.response.OrderDetailResponseDto;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.QOrderDetail;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class OrderDetailRepositoryImpl extends QuerydslRepositorySupport implements OrderDetailRepositoryCustom {

    public OrderDetailRepositoryImpl() {
        super(OrderDetail.class);
    }

    private final QOrder qOrder = QOrder.order;
    private final QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final QBook qBook = QBook.book;

    @Override
    public List<OrderDetailResponseDto> findByOrderId(Long orderId) {
        JPQLQuery<OrderDetailResponseDto> query = from(qOrderDetail)
                .join(qOrderDetail.order, qOrder) 
                .join(qOrderDetail.book, qBook)
                .where(qOrder.orderId.eq(orderId))
                .select(Projections.constructor(OrderDetailResponseDto.class,
                        qOrderDetail.orderDetailId,
                        qOrder.orderDate,
                        qOrder.orderState.name.stringValue(), // Enum을 문자열로 변환
                        qBook.title,
                        qOrderDetail.quantity,
                        qOrderDetail.finalPrice
                ));

        return query.fetch();

    }

    @Override
    public Page<OrderDetailResponseDto> findByCustomerId(Pageable pageable, Long customerId) {
        JPAQuery<OrderDetailResponseDto> query = new JPAQuery<>(getEntityManager());

        // 기본 쿼리 작성
        query.from(qOrderDetail)
                .join(qOrderDetail.order, qOrder)
                .join(qOrderDetail.book, qBook)
                .where(qOrder.customer.id.eq(customerId))
                .select(Projections.constructor(OrderDetailResponseDto.class,
                        qOrderDetail.orderDetailId,
                        qOrder.orderDate,
                        qOrder.orderState.name.stringValue(),
                        qBook.title,
                        qOrderDetail.quantity,
                        qOrderDetail.finalPrice
                ));

        OrderSpecifier<?> orderSpecifier;
        orderSpecifier = qOrderDetail.orderDetailId.desc();
        query.orderBy(orderSpecifier);

        // 전체 개수 계산
        long total = query.fetchCount();

        // 페이징 처리
        List<OrderDetailResponseDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }



}

