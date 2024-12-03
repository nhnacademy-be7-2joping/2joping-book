package com.nhnacademy.bookstore.user.member.repository;


import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.common.error.exception.user.member.status.MemberNothingToUpdateException;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.dto.request.MemberUpdateRequesteDto;
import com.nhnacademy.bookstore.user.member.dto.response.MemberUpdateResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.repository.impl.MemberQuerydslRepositoryImpl;
import com.nhnacademy.bookstore.user.memberstatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * MemberQuerydslRepositoryImplTest
 * 이 클래스는 MemberQuerydslRepositoryImpl의 동작을 테스트합니다.
 * 회원 정보 조회 및 업데이트 기능의 동작을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberQuerydslRepositoryImplTest {

    @Autowired
    private MemberQuerydslRepositoryImpl memberQuerydslRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 테스트 데이터 초기화
     * 회원, 등급, 상태 데이터를 데이터베이스에 저장합니다.
     *
     */
    @BeforeEach
    void setUp() {

        MemberStatus status = new MemberStatus(1L, "가입");
        entityManager.merge(status);

        MemberTier tier = new MemberTier(1L, Tier.NORMAL, true, 1, 1, 1);
        entityManager.merge(tier);

        // 2. 강제로 flush() 및 clear() 호출
        entityManager.flush();
        entityManager.clear();

        // 3. 저장한 엔티티를 다시 조회
        MemberStatus persistedStatus = entityManager.find(MemberStatus.class, 1L);
        MemberTier persistedTier = entityManager.find(MemberTier.class, 1L);

        // Member 엔티티를 저장
        Member member = new Member(
                "testLoginId", "Test Member", "nick", Gender.M, LocalDate.now(),
                10, LocalDate.now(), null, false, 0, 0, null,
                null, persistedStatus, persistedTier, null
        );
        member.initializeCustomerFields("이한빈", "010-2222-1223", "dlgksqls@naver.com");
        entityManager.persist(member);

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 회원 정보 조회 테스트
     * 예상 결과: 회원 ID로 조회된 정보가 예상 값과 일치합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 정보 조회 - 성공")
    @Transactional
    void testGetMemberInfo() {
        // given
        long memberId = 1L;

        // when
        MemberUpdateResponseDto result = memberQuerydslRepository.getMemberInfo(memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("이한빈");
        assertThat(result.email()).isEqualTo("dlgksqls@naver.com");
        assertThat(result.phone()).isEqualTo("010-2222-1223");
    }

    /**
     * 회원 정보 업데이트 테스트 - 모든 필드 업데이트
     * 예상 결과: 회원 정보가 요청된 값으로 모두 업데이트됩니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 정보 업데이트 - 성공")
    void testUpdateMemberDetails_success() {
        // given
        long memberId = 1L;
        MemberUpdateRequesteDto updateRequest = new MemberUpdateRequesteDto("010-3333-4444", "newemail@example.com", "newNick");

        // when
        MemberUpdateResponseDto result = memberQuerydslRepository.updateMemberDetails(updateRequest, memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.phone()).isEqualTo("010-3333-4444");
        assertThat(result.email()).isEqualTo("newemail@example.com");
        assertThat(result.nickName()).isEqualTo("newNick");
    }

    /**
     * 회원 정보 업데이트 테스트 - 업데이트할 데이터가 없는 경우
     * 예상 결과: MemberNothingToUpdateException 발생
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 정보 업데이트 - 업데이트할 데이터 없음")
    void testUpdateMemberDetails_noUpdateData() {
        // given
        long memberId = 1L;
        MemberUpdateRequesteDto updateRequest = new MemberUpdateRequesteDto(null, null, null);

        // when / then
        assertThatThrownBy(() -> memberQuerydslRepository.updateMemberDetails(updateRequest, memberId))
                .isInstanceOf(MemberNothingToUpdateException.class)
                .hasMessageContaining("업데이트할 데이터가 없습니다.");
    }

    /**
     * 회원 정보 업데이트 테스트 - 일부 필드만 업데이트
     * 예상 결과: 업데이트되지 않은 필드는 기존 값을 유지합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 정보 업데이트 - 일부 필드 업데이트")
    void testUpdateMemberDetails_partialUpdate() {
        // given
        long memberId = 1L;
        MemberUpdateRequesteDto updateRequest = new MemberUpdateRequesteDto(null, "updatedemail@example.com", null);

        // when
        MemberUpdateResponseDto result = memberQuerydslRepository.updateMemberDetails(updateRequest, memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("updatedemail@example.com");
        assertThat(result.phone()).isEqualTo("010-2222-1223"); // 기존 값 유지
        assertThat(result.nickName()).isEqualTo("nick"); // 기존 값 유지
    }
}