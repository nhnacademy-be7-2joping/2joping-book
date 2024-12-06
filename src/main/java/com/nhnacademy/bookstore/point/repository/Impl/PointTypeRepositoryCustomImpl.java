package com.nhnacademy.bookstore.point.repository.Impl;

import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.entity.QPointType;
import com.nhnacademy.bookstore.point.repository.PointTypeRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PointTypeRepositoryCustomImpl implements PointTypeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetPointTypeResponse> findAllActivePointTypes() {
        QPointType pointType = QPointType.pointType;

        return queryFactory
                .select(Projections.constructor(GetPointTypeResponse.class,
                        pointType.pointTypeId,
                        pointType.type,
                        pointType.accVal,
                        pointType.name,
                        pointType.isActive
                ))
                .from(pointType)
                .where(pointType.isActive.isTrue())
                .fetch();
    }
}
