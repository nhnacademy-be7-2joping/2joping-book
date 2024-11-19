package com.nhnacademy.bookstore.user.member.repository.impl;

import com.nhnacademy.bookstore.common.error.enums.RedirectType;
import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberNothingToUpdateException;
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
import org.springframework.stereotype.Repository;

/**
 * 회원 관련 QueryDSL 기반의 커스텀 Repository 구현체입니다.
 * <p>회원 정보 업데이트 및 조회 기능을 제공합니다.</p>
 *
 * @author Luha
 * @since 1.0
 */
@Repository
@RequiredArgsConstructor
public class MemberQuerydslRepositoryImpl implements MemberQuerydslRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    /**
     * 회원 정보를 업데이트합니다.
     * <p>전화번호, 이메일, 닉네임 중 하나라도 변경 사항이 있어야 업데이트를 실행합니다.</p>
     *
     * @param dto 수정할 회원 정보 데이터
     * @param customerId 수정할 회원의 고유 ID
     * @return 업데이트된 회원 정보를 담은 DTO
     * @throws MemberNothingToUpdateException 업데이트할 데이터가 없는 경우 발생
     */
    @Transactional
    @Override
    public MemberUpdateResponseDto updateMemberDetails(MemberUpdateRequesteDto dto, long customerId) {
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
            throw new MemberNothingToUpdateException("업데이트할 데이터가 없습니다.", RedirectType.REDIRECT, "/mypage/edit-profile");
        }

        updateClause.where(QMember.member.id.eq(customerId)).execute();

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
                .where(QMember.member.id.eq(customerId))
                .fetchOne();
    }

    /**
     * 회원 정보를 조회합니다.
     *
     * @param customerId 조회할 회원의 고유 ID
     * @return 회원 정보를 담은 DTO
     */
    @Override
    public MemberUpdateResponseDto getMemberInfo(long customerId) {
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
                .where(QMember.member.id.eq(customerId))
                .fetchOne();
    }
}
