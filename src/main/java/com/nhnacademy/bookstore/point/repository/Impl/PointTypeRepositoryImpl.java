package com.nhnacademy.bookstore.point.repository.Impl;

import com.nhnacademy.bookstore.point.dto.response.PointTypeDto;
import com.nhnacademy.bookstore.point.dto.response.ReadPointTypeResponseDto;
import com.nhnacademy.bookstore.point.entity.QPointType;
import com.nhnacademy.bookstore.point.repository.PointTypeRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PointTypeRepositoryImpl implements PointTypeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PointTypeDto> findAllActivePointTypes() {
        QPointType pointType = QPointType.pointType;

        return queryFactory
                .select(Projections.constructor(PointTypeDto.class,
                        pointType.id,
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
