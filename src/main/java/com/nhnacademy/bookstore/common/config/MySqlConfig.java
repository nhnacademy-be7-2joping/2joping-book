package com.nhnacademy.bookstore.common.config;

import com.nhnacademy.bookstore.common.dto.response.MysqlKeyResponseDto;
import com.nhnacademy.bookstore.common.service.KeyManagerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class MySqlConfig {

    private final KeyManagerService keyManagerService;
    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MAX_THREADS = 200;

    @Bean
    public DataSource dataSource() {
        MysqlKeyResponseDto keyResponseDto = keyManagerService.getDbConnectionInfo();

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(keyResponseDto.url());
        dataSource.setUsername(keyResponseDto.username());
        dataSource.setPassword(keyResponseDto.password());

        // DBCP2 커넥션 풀 설정
        configureConnectionPool(dataSource);
        configureConnectionValidation(dataSource);
        configureConnectionManagement(dataSource);

        return dataSource;
    }

    private void configureConnectionPool(BasicDataSource dataSource) {
        dataSource.setInitialSize(CORE_COUNT * 2);
        dataSource.setMaxTotal(MAX_THREADS);
        dataSource.setMaxIdle(MAX_THREADS);
        dataSource.setMinIdle(MAX_THREADS / 10);
    }

    private void configureConnectionValidation(BasicDataSource dataSource) {
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);

        dataSource.setValidationQuery("SELECT 1");
        dataSource.setValidationQueryTimeout(Duration.ofSeconds(3));
    }

    private void configureConnectionManagement(BasicDataSource dataSource) {
        dataSource.setMaxWait(Duration.ofSeconds(10));
        dataSource.setRemoveAbandonedTimeout(Duration.ofSeconds(30));
        dataSource.setRemoveAbandonedOnBorrow(true);

        dataSource.setTimeBetweenEvictionRunsMillis(300000); // 5분
        dataSource.setMinEvictableIdle(Duration.ofMinutes(10));
    }

    private int calculateOptimalMaxConnections() {
        int baseConnections = (CORE_COUNT * 4) + 1;
        return Math.min(baseConnections + 2, 100);
    }
}
