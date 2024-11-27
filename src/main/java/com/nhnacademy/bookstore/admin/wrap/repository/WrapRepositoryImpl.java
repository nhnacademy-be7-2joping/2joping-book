package com.nhnacademy.bookstore.admin.wrap.repository;

import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.QWrap;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WrapRepositoryImpl extends QuerydslRepositorySupport implements WrapRepositoryCustom {

    public WrapRepositoryImpl() {
        super(Wrap.class);
    }

    @Override
    public List<WrapResponseDto> findAllByIsActiveTrue() {
        QWrap wrap = QWrap.wrap;

        return from(wrap)
                .where(wrap.isActive.isTrue())
                .select(Projections.constructor(WrapResponseDto.class,
                        wrap.wrapId,
                        wrap.name,
                        wrap.wrapPrice,
                        wrap.isActive))
                .fetch();
    }
}
