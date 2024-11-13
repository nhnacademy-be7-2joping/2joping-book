package com.nhnacademy.bookstore.admin.wrap.repository;

import com.nhnacademy.bookstore.admin.wrap.dto.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.QWrap;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.nhnacademy.bookstore.admin.wrap.repository.WrapRepositoryCustom;
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
    public List<WrapResponseDto> getAllWraps() {
        QWrap wrap = QWrap.wrap;

        // Querydsl을 사용해 Wrap 데이터를 조회하고, WrapResponseDto로 매핑
        return from(wrap)
                .select(Projections.constructor(WrapResponseDto.class,
                        wrap.wrapId,
                        wrap.name,
                        wrap.wrapPrice,
                        wrap.isActive))
                .fetch();
    }
}
