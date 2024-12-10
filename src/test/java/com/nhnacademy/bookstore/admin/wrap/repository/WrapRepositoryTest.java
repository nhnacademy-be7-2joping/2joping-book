package com.nhnacademy.bookstore.admin.wrap.repository;

import com.nhnacademy.bookstore.admin.wrap.dto.response.WrapResponseDto;
import com.nhnacademy.bookstore.admin.wrap.entity.Wrap;
import com.nhnacademy.bookstore.common.config.MySqlConfig;
import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class) // QueryDSL 설정 추가
@ActiveProfiles("test") // 테스트 프로파일 활성화
@ImportAutoConfiguration(exclude = MySqlConfig.class) // MySQL 설정 제외
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WrapRepositoryImplTest {

    @Autowired
    private WrapRepositoryImpl wrapRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 생성
        Wrap activeWrap1 = new Wrap("포장 상품 1", 1000, true);
        Wrap activeWrap2 = new Wrap("포장 상품 2", 2000, true);
        Wrap inactiveWrap = new Wrap("포장 상품 3", 3000, false);

        // 데이터 저장
        entityManager.persist(activeWrap1);
        entityManager.persist(activeWrap2);
        entityManager.persist(inactiveWrap);

        // flush 및 clear
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindAllByIsActiveTrue() {
        // When
        List<WrapResponseDto> activeWraps = wrapRepository.findAllByIsActiveTrue();

        // Then
        assertThat(activeWraps).hasSize(2); // 활성화된 데이터만 반환
        assertThat(activeWraps.get(0).name()).isEqualTo("포장 상품 1");
        assertThat(activeWraps.get(1).name()).isEqualTo("포장 상품 2");
    }
}
