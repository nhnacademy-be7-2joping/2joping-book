package com.nhnacademy.bookstore.user.member.repository;


import com.nhnacademy.bookstore.common.config.MySqlConfig;
import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import com.nhnacademy.bookstore.user.enums.Gender;
import com.nhnacademy.bookstore.user.member.dto.response.address.MemberAddressResponseDto;
import com.nhnacademy.bookstore.user.member.entity.Member;
import com.nhnacademy.bookstore.user.member.entity.MemberAddress;
import com.nhnacademy.bookstore.user.member.repository.impl.MemberAddressQuerydslRepositoryImpl;
import com.nhnacademy.bookstore.user.memberstatus.entity.MemberStatus;
import com.nhnacademy.bookstore.user.tier.entity.MemberTier;
import com.nhnacademy.bookstore.user.tier.enums.Tier;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MemberAddressQuerydslRepositoryImplTest
 * 이 클래스는 MemberAddressQuerydslRepositoryImpl의 동작을 테스트합니다.
 * 회원 ID를 기반으로 주소 목록을 조회하는 기능을 검증합니다.
 *
 * @since 1.0
 * author Luha
 */
@DataJpaTest
@Import(QuerydslConfig.class) // QueryDSL 및 설정 클래스 포함
@ActiveProfiles("test") // test 프로파일 활성화
@ImportAutoConfiguration(exclude = MySqlConfig.class) // MySqlConfig 비활성화
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberAddressQuerydslRepositoryImplTest {

    @Autowired
    private MemberAddressQuerydslRepositoryImpl memberAddressQuerydslRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 테스트 데이터 초기화
     * 회원, 등급, 상태, 주소 데이터를 데이터베이스에 저장합니다.
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

        // 테스트 데이터 초기화
        Member member = new Member("testLoginId", "Test Member", "nick", Gender.M, LocalDate.now(), 10, LocalDate.now(), null, false, 0, 0, null,
        null, persistedStatus, persistedTier, null);
        member.initializeCustomerFields("이한빈", "010-2222-1223", "dlgksqls@naver.com");
        entityManager.merge(member);


        // 2. 강제로 flush() 및 clear() 호출
        entityManager.flush();
        entityManager.clear();

        // 3. 저장한 엔티티를 다시 조회
        Member persistMember = entityManager.find(Member.class, 1L);
        MemberAddress address1 = new MemberAddress(
                null,
                "12345",
                "Road Address 1",
                "Detail Address 1",
                "Home",
                true,
                "Receiver 1"
                , true,
                persistMember
        );
        entityManager.persist(address1);

        MemberAddress address2 = new MemberAddress(
                null,
                "67890",
                "Road Address 2",
                "Detail Address 2",
                "Work",
                false,
                "Receiver 2",
                true,
                persistMember
        );
        entityManager.persist(address2);

        entityManager.flush(); // 데이터 반영
        entityManager.clear(); // 영속성 컨텍스트 초기화
    }

    /**
     * 회원 ID를 기반으로 주소 목록 조회 테스트
     * 예상 결과: 회원 ID에 연결된 주소가 올바르게 조회되고, 각 주소의 세부 정보가 일치해야 합니다.
     *
     * @since 1.0
     * author Luha
     */
    @Test
    @DisplayName("회원 ID를 기반으로 주소 목록 조회")
    @Transactional
    void testFindAddressesByMemberId() {
        // given
        long memberId = 1L;

        // when
        List<MemberAddressResponseDto> result = memberAddressQuerydslRepository.findAddressesByMemberId(memberId);

        // then
        assertThat(result).hasSize(2); // 주소 2개가 반환되어야 함
        assertThat(result.get(0).isDefaultAddress()).isTrue(); // 첫 번째 주소는 defaultAddress
        assertThat(result.get(1).addressAlias()).isEqualTo("Work"); // 두 번째 주소의 별칭 확인
    }
}
