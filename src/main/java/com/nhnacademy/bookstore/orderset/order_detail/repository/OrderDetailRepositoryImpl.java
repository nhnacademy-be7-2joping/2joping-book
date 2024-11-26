package com.nhnacademy.bookstore.orderset.order_detail.repository;

import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.orderset.order.entity.QOrder;
import com.nhnacademy.bookstore.orderset.order_detail.dto.response.OrderDetailResponseDto;
import com.nhnacademy.bookstore.orderset.order_detail.entity.OrderDetail;
import com.nhnacademy.bookstore.orderset.order_detail.entity.QOrderDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
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
                .join(qOrderDetail.order, qOrder) // 일반 조인
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
}

