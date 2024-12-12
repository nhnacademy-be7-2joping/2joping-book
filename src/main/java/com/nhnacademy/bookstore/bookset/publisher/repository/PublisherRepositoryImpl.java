package com.nhnacademy.bookstore.bookset.publisher.repository;

import com.nhnacademy.bookstore.bookset.publisher.dto.response.PublisherResponseDto;
import com.nhnacademy.bookstore.bookset.publisher.entity.Publisher;
import com.nhnacademy.bookstore.bookset.publisher.entity.QPublisher;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;


public class PublisherRepositoryImpl extends QuerydslRepositorySupport implements PublisherRepositoryCustom {

    private final EntityManager entityManager;
    private final QPublisher publisher = QPublisher.publisher;

    @Autowired
    public PublisherRepositoryImpl(EntityManager entityManager) {
        super(Publisher.class);
        this.entityManager = entityManager;
    }

    @Override
    public Page<PublisherResponseDto> findAllBy(Pageable pageable) {
        // PublisherResponseDto 리스트를 조회하는 쿼리
        JPAQuery<PublisherResponseDto> query = new JPAQuery<>(entityManager);

        // 정렬 조건 추가
        query.select(Projections.constructor(PublisherResponseDto.class,
                        publisher.publisherId,
                        publisher.name
                ))
                .from(publisher)
                .orderBy(publisher.publisherId.asc())  // 정렬 조건 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<PublisherResponseDto> results = query.fetch();

        // 전체 개수를 조회하는 쿼리
        long total = query.fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

}