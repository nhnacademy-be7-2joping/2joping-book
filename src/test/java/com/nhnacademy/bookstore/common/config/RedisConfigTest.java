package com.nhnacademy.bookstore.common.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RedisConfigTest 클래스는 RedisConfig 클래스의 RedisTemplate 설정을 검증하기 위한 테스트 클래스입니다.
 * 주요 테스트 항목:
 * - RedisTemplate Bean이 생성되고, 설정 값이 올바르게 적용되는지 확인
 * - Key, Value, HashKey, HashValue에 대한 직렬화 설정 검증
 * - RedisConnectionFactory가 RedisTemplate에 올바르게 연결되는지 확인
 *
 * @author Luha
 * @version 1.0
 */
class RedisConfigTest {

    @Mock
    private RedisConnectionFactory connectionFactory;

    /**
     * 테스트: RedisTemplate Bean 생성 및 설정 값 확인
     * 예상 결과: RedisTemplate 객체가 생성되고, 직렬화 설정이 올바르게 적용된다.
     */
    @Test
    @DisplayName("RedisTemplate Bean 생성 테스트")
    void testRedisTemplateBeanCreation() {
        // given
        RedisConfig redisConfig = new RedisConfig();

        // when
        RedisTemplate<Object, Object> redisTemplate = redisConfig.redisTemplate(connectionFactory);

        // then
        assertThat(redisTemplate).isNotNull();
        assertThat(redisTemplate.getKeySerializer()).isInstanceOf(StringRedisSerializer.class);
        assertThat(redisTemplate.getValueSerializer()).isInstanceOf(GenericJackson2JsonRedisSerializer.class);
        assertThat(redisTemplate.getHashKeySerializer()).isInstanceOf(StringRedisSerializer.class);
        assertThat(redisTemplate.getHashValueSerializer()).isInstanceOf(GenericJackson2JsonRedisSerializer.class);
        assertThat(redisTemplate.getConnectionFactory()).isEqualTo(connectionFactory);
    }
}