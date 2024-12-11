package com.nhnacademy.bookstore.refund.repository.impl;

import com.nhnacademy.bookstore.bookset.book.entity.QBook;
import com.nhnacademy.bookstore.orderset.order.entity.QOrder;
import com.nhnacademy.bookstore.orderset.orderdetail.entity.QOrderDetail;
import com.nhnacademy.bookstore.refund.dto.response.RefundResponseDto;
import com.nhnacademy.bookstore.refund.entity.QRefundHistory;
import com.nhnacademy.bookstore.refund.entity.QRefundPolicy;
import com.nhnacademy.bookstore.refund.repository.RefundQuerydslRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RefundQuerydslRepositoryImpl implements RefundQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<RefundResponseDto> findRefundHistories(Long customerId) {
        QRefundHistory refundHistory = QRefundHistory.refundHistory;
        QRefundPolicy refundPolicy = QRefundPolicy.refundPolicy;
        QOrder order = QOrder.order;
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        QBook book = QBook.book;

        return queryFactory.select(Projections.constructor(RefundResponseDto.class,
                        refundHistory.refundHistoryId,
                        refundPolicy.policy.stringValue(),
                        // 연결된 책 제목을 ","로 합치기
                        Expressions.stringTemplate("group_concat({0})", book.title),
                        refundHistory.createdAt,
                        refundHistory.approvedAt,
                        refundHistory.isApproved
                ))
                .from(refundHistory)
                .join(refundHistory.order, order)
                .join(order.orderDetails, orderDetail)
                .join(orderDetail.book, book)
                .join(refundHistory.refundPolicy, refundPolicy)
                .where(order.customer.id.eq(customerId)) // customer_id로 필터링
                .groupBy(refundHistory.refundHistoryId, refundPolicy.policy, refundHistory.isApproved)
                .fetch();
    }
}
