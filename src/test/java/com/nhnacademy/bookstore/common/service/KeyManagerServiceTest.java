package com.nhnacademy.bookstore.common.service;

import com.nhnacademy.bookstore.common.client.SecretDataClient;
import com.nhnacademy.bookstore.common.config.properties.MysqlKeyManagerConfig;
import com.nhnacademy.bookstore.common.dto.response.MysqlKeyResponseDto;
import com.nhnacademy.bookstore.common.dto.response.SecretResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * KeyManagerServiceTest 클래스는 KeyManagerService의 동작을 검증하기 위한 테스트 클래스입니다.
 * 주요 테스트 항목:
 * - DB 연결 정보를 올바르게 가져오는지 확인 (`getDbConnectionInfo` 메서드)
 * - `SecretDataClient`와 `MysqlKeyManagerConfig`를 Mocking하여 의존성 주입 및 동작 검증
 * - ReflectionTestUtils를 사용하여 @Value 필드의 테스트 데이터를 동적으로 설정
 *
 * @author Luha
 * @version 1.0
 */
class KeyManagerServiceTest {

    private KeyManagerService keyManagerService;
    private SecretDataClient secretDataClient;
    private MysqlKeyManagerConfig mysqlKeyManagerConfig;

    @BeforeEach
    void setUp() {
        secretDataClient = mock(SecretDataClient.class);
        mysqlKeyManagerConfig = mock(MysqlKeyManagerConfig.class);
        keyManagerService = new KeyManagerService(secretDataClient, mysqlKeyManagerConfig);

        // Reflection을 사용하여 @Value 필드 주입
        ReflectionTestUtils.setField(keyManagerService, "appKey", "testAppKey");
        ReflectionTestUtils.setField(keyManagerService, "accessKeyId", "testAccessKeyId");
        ReflectionTestUtils.setField(keyManagerService, "secretAccessKey", "testSecretAccessKey");
    }

    @Test
    @DisplayName("DB 연결 정보 가져오기 - 성공")
    void testGetDbConnectionInfo() {
        // Arrange
        String urlKey = "urlKey";
        String usernameKey = "usernameKey";
        String passwordKey = "passwordKey";

        String url = "jdbc:mysql://localhost:3306/testdb";
        String username = "testUser";
        String password = "testPassword";

        when(mysqlKeyManagerConfig.getUrl()).thenReturn(urlKey);
        when(mysqlKeyManagerConfig.getUsername()).thenReturn(usernameKey);
        when(mysqlKeyManagerConfig.getPassword()).thenReturn(passwordKey);

        when(secretDataClient.getSecret("testAppKey", urlKey, "testAccessKeyId", "testSecretAccessKey"))
                .thenReturn(new SecretResponseDto(new SecretResponseDto.Header(200, "Success", true),
                        new SecretResponseDto.Body(url)));
        when(secretDataClient.getSecret("testAppKey", usernameKey, "testAccessKeyId", "testSecretAccessKey"))
                .thenReturn(new SecretResponseDto(new SecretResponseDto.Header(200, "Success", true),
                        new SecretResponseDto.Body(username)));
        when(secretDataClient.getSecret("testAppKey", passwordKey, "testAccessKeyId", "testSecretAccessKey"))
                .thenReturn(new SecretResponseDto(new SecretResponseDto.Header(200, "Success", true),
                        new SecretResponseDto.Body(password)));

        // Act
        MysqlKeyResponseDto response = keyManagerService.getDbConnectionInfo();

        // Assert
        assertEquals(url, response.url());
        assertEquals(username, response.username());
        assertEquals(password, response.password());

        verify(secretDataClient, times(1)).getSecret("testAppKey", urlKey, "testAccessKeyId", "testSecretAccessKey");
        verify(secretDataClient, times(1)).getSecret("testAppKey", usernameKey, "testAccessKeyId", "testSecretAccessKey");
        verify(secretDataClient, times(1)).getSecret("testAppKey", passwordKey, "testAccessKeyId", "testSecretAccessKey");
    }
}