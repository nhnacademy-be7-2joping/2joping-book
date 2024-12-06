package com.nhnacademy.bookstore.point.repository;

import com.nhnacademy.bookstore.point.entity.PointHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PointHistoryRepositoryTest {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @BeforeEach
    void setUp() {
        PointHistory pointHistory = PointHistory.builder()
                .customerId(1L)
                .pointVal(100)
                .registerDate(LocalDateTime.now())
                .build();
        pointHistoryRepository.save(pointHistory);
    }

    @Test
    void findAllByCustomerIdOrderByRegisterDateDesc_Success() {
        List<PointHistory> result = pointHistoryRepository.findAllByCustomerIdOrderByRegisterDateDesc(1L);

        assertEquals(1, result.size());
    }
}
