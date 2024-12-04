package com.nhnacademy.bookstore.common.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AppConfigTest 클래스는 AppConfig 클래스의 동작을 검증하기 위한 테스트 클래스입니다.
 * 주요 테스트 항목:
 * - PasswordEncoder Bean의 생성 여부 확인
 * - 생성된 PasswordEncoder의 올바른 동작 확인
 *
 * @author Luha
 * @version 1.0
 */
class AppConfigTest {

    /**
     * 테스트: PasswordEncoder Bean 생성 및 작동 확인
     * 예상 결과: PasswordEncoder가 생성되고, 올바르게 작동한다.
     */
    @Test
    @DisplayName("PasswordEncoder Bean 생성 테스트")
    void testPasswordEncoderBeanCreation() {
        // given
        AppConfig appConfig = new AppConfig();

        // when
        PasswordEncoder passwordEncoder = appConfig.passwordEncoder();

        // then
        assertThat(passwordEncoder).isNotNull();
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(encodedPassword).isNotEqualTo(rawPassword); // 인코딩된 값은 원본과 달라야 함
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue(); // 매칭 테스트
    }
}