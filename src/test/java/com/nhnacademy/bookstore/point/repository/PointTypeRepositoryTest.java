package com.nhnacademy.bookstore.point.repository;

import com.nhnacademy.bookstore.point.dto.response.GetPointTypeResponse;
import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import com.nhnacademy.bookstore.common.config.QuerydslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class) // QueryDSL 및 설정 클래스 포함
@ActiveProfiles("test") // test 프로파일 활성화
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PointTypeRepositoryTest {

    @Autowired
    private PointTypeRepositoryCustom pointTypeRepository;

    @Autowired
    private EntityManager entityManager;

    private PointType activePointType1;
    private PointType activePointType2;
    private PointType inactivePointType;

    @BeforeEach
    void setUp() {
        // 활성화된 PointType 생성
        activePointType1 = PointType.builder()
                .type(PointTypeEnum.PERCENT)
                .accVal(10)
                .name("Active Point Type 1")
                .isActive(true)
                .build();

        activePointType2 = PointType.builder()
                .type(PointTypeEnum.ACTUAL)
                .accVal(15)
                .name("Active Point Type 2")
                .isActive(true)
                .build();

        // 비활성화된 PointType 생성
        inactivePointType = PointType.builder()
                .type(PointTypeEnum.PERCENT)
                .accVal(20)
                .name("Inactive Point Type")
                .isActive(false)
                .build();

        // Entity 저장
        entityManager.persist(activePointType1);
        entityManager.persist(activePointType2);
        entityManager.persist(inactivePointType);

        // 강제로 flush 및 clear 호출
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("활성화된 PointType 조회 테스트")
    void testFindAllActivePointTypes() {
        // 메서드 호출
        List<GetPointTypeResponse> activePointTypes = pointTypeRepository.findAllActivePointTypes();

        // 검증
        assertThat(activePointTypes).hasSize(2); // 활성화된 PointType만 조회됨
        assertThat(activePointTypes).extracting("name")
                .containsExactlyInAnyOrder("Active Point Type 1", "Active Point Type 2");
        assertThat(activePointTypes).extracting("isActive").containsOnly(true);
    }
}
