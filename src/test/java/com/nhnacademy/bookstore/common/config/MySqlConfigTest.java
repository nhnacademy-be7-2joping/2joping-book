package com.nhnacademy.bookstore.common.config;


import com.nhnacademy.bookstore.common.dto.response.MysqlKeyResponseDto;
import com.nhnacademy.bookstore.common.service.KeyManagerService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * MySqlConfigTest 클래스는 MySqlConfig 클래스의 동작을 검증하기 위한 테스트 클래스입니다.
 * 주요 테스트 항목:
 * - KeyManagerService에서 반환된 데이터가 DataSource 객체에 올바르게 설정되는지 확인
 * - DataSource 설정 값이 기대한 대로 초기화되고 적용되는지 검증
 * 사용된 기술:
 * - Mockito를 사용하여 KeyManagerService의 동작을 모킹
 * - JUnit 5 및 AssertJ를 통해 테스트 검증
 *
 * @author Luha
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class MySqlConfigTest {

    @Mock
    private KeyManagerService keyManagerService;

    @InjectMocks
    private MySqlConfig mySqlConfig;

    /**
     * 테스트: DataSource Bean 생성 및 설정 값 확인
     * 예상 결과: DataSource 객체가 생성되고, 설정 값이 올바르게 적용된다.
     */
    @Test
    @DisplayName("DataSource Bean 생성 테스트")
    void testDataSourceBeanCreation() {
        int coreCount = Runtime.getRuntime().availableProcessors();

        // given
        MysqlKeyResponseDto mockResponse = new MysqlKeyResponseDto(
                "jdbc:mysql://localhost:3306/testdb",
                "testUser",
                "testPassword"
        );
        when(keyManagerService.getDbConnectionInfo()).thenReturn(mockResponse);

        // when
        DataSource dataSource = mySqlConfig.dataSource();

        // then
        assertThat(dataSource).isNotNull();
        assertThat(dataSource).isInstanceOf(BasicDataSource.class);

        BasicDataSource basicDataSource = (BasicDataSource) dataSource;
        assertThat(basicDataSource.getUrl()).isEqualTo("jdbc:mysql://localhost:3306/testdb");
        assertThat(basicDataSource.getUsername()).isEqualTo("testUser");
        assertThat(basicDataSource.getPassword()).isEqualTo("testPassword");
        assertThat(basicDataSource.getInitialSize()).isEqualTo(coreCount * 2); // 예를 들어 16이 나올 경우
        assertThat(basicDataSource.getMaxTotal()).isEqualTo((200));
        assertThat(basicDataSource.getMaxIdle()).isEqualTo(200);
        assertThat(basicDataSource.getMinIdle()).isEqualTo(20);
        assertThat(basicDataSource.getMaxWaitDuration()).isEqualTo(Duration.ofSeconds(10));
    }
}