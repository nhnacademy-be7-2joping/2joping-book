package com.nhnacademy.bookstore.point.repository;

import com.nhnacademy.bookstore.point.entity.PointType;
import com.nhnacademy.bookstore.point.enums.PointTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PointTypeRepositoryTest {

    @Autowired
    private PointTypeRepository pointTypeRepository;

    @BeforeEach
    void setUp() {
        PointType pointType = PointType.builder()
                .type(PointTypeEnum.ACTUAL)
                .accVal(100)
                .name("리뷰작성")
                .isActive(true)
                .build();
        pointTypeRepository.save(pointType);
    }

    @Test
    void findByNameAndIsActiveTrue_Success() {
        Optional<PointType> result = pointTypeRepository.findByNameAndIsActiveTrue("리뷰작성");

        assertTrue(result.isPresent());
        assertEquals("리뷰작성", result.get().getName());
    }
}
