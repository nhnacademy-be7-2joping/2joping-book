package com.nhnacademy.bookstore.common.service;

import com.nhnacademy.bookstore.common.client.SecretDataClient;
import com.nhnacademy.bookstore.common.config.properties.MysqlKeyManagerConfig;
import com.nhnacademy.bookstore.common.dto.response.MysqlKeyResponseDto;
import com.nhnacademy.bookstore.common.dto.response.SecretResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty()
public class KeyManagerService {
    private final SecretDataClient secretDataClient;
    private final MysqlKeyManagerConfig mysqlKeyManagerConfig;

    @Value("${keymanager.appkey}")
    private String appKey;

    @Value("${keymanager.access-key-id}")
    private String accessKeyId;

    @Value("${keymanager.secret-access-key}")
    private String secretAccessKey;

    public MysqlKeyResponseDto getDbConnectionInfo() {
        SecretResponseDto urlResponse = secretDataClient.getSecret(appKey, mysqlKeyManagerConfig.getUrl(), accessKeyId,
                                                                   secretAccessKey
        );
        SecretResponseDto usernameResponse = secretDataClient.getSecret(appKey, mysqlKeyManagerConfig.getUsername(),
                                                                        accessKeyId,
                                                                        secretAccessKey
        );
        SecretResponseDto passwordResponse = secretDataClient.getSecret(appKey, mysqlKeyManagerConfig.getPassword(),
                                                                        accessKeyId,
                                                                        secretAccessKey
        );

        return new MysqlKeyResponseDto(urlResponse.body().secret(), usernameResponse.body().secret(),
                                       passwordResponse.body().secret()
        );
    }
}
