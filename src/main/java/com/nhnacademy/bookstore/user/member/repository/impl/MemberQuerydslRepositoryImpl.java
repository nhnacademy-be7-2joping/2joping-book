package com.nhnacademy.bookstore.user.member.repository.impl;

import com.nhnacademy.bookstore.user.customer.entity.Customer;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberUpdateResponseDto;
import com.nhnacademy.bookstore.user.member.entity.QMember;
import com.nhnacademy.bookstore.user.member.repository.MemberQuerydslRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberQuerydslRepositoryImpl implements MemberQuerydslRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public MemberUpdateResponseDto updateMemberDetails(MemberUpdateRequesteDto dto, long memberId) {
        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, QMember.member);

        boolean isUpdated = false;

        if (dto.phone() != null) {
            updateClause.set(QMember.member.phone, dto.phone());
            isUpdated = true;
        }
        if (dto.email() != null) {
            updateClause.set(QMember.member.email, dto.email());
            isUpdated = true;
        }
        if (dto.nickName() != null) {
            updateClause.set(QMember.member.nickname, dto.nickName());
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new IllegalArgumentException("업데이트할 데이터가 없습니다.");
        }

        updateClause.where(QMember.member.id.eq(memberId)).execute();

        // Fetch updated data
        return queryFactory
                .select(Projections.constructor(MemberUpdateResponseDto.class,
                        QMember.member.name,
                        QMember.member.gender,
                        QMember.member.birthday,
                        QMember.member.phone,
                        QMember.member.email,
                        QMember.member.nickname
                ))
                .from(QMember.member)
                .where(QMember.member.id.eq(memberId))
                .fetchOne();
    }
}
